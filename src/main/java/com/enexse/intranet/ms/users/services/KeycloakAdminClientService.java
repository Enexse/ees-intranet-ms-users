package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.configurations.keycloakProvider;
import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.controllers.EesUsersController;
import com.enexse.intranet.ms.users.enums.EesStatusUser;
import com.enexse.intranet.ms.users.exceptions.ObjectFoundException;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserRole;
import com.enexse.intranet.ms.users.payload.request.EesCreateUserRequest;
import com.enexse.intranet.ms.users.payload.request.EesLoginRequest;
import com.enexse.intranet.ms.users.repositories.EesRoleRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KeycloakAdminClientService {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(EesUsersController.class);
    private final keycloakProvider kcProvider;
    private final EesUserRepository eesUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EesRoleRepository eesRoleRepository;

    @Value("${keycloak.realm}")
    public String realm;

    @Autowired
    EesMailService mailService;

    public KeycloakAdminClientService(keycloakProvider keycloakProvider, EesUserRepository eesUserRepository, PasswordEncoder passwordEncoder, EesRoleRepository eesRoleRepository) {
        this.kcProvider = keycloakProvider;
        this.eesUserRepository = eesUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.eesRoleRepository = eesRoleRepository;
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public boolean verifyPersonalEmail(String email) {
        Optional<EesUser> eesUser = eesUserRepository.findByPersonalEmail(email);
        return eesUser.isPresent();
    }

    public Response createKeycloakUser(EesCreateUserRequest request, String personalEmail) throws Exception {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        String password = request.getFirstName() + "@1234";
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(password);

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(request.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(request.getFirstName());
        kcUser.setLastName(request.getLastName());
        kcUser.setEmail(request.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        try {
            Optional<EesUser> eesUser = eesUserRepository.findByPersonalEmail(personalEmail);
            if (eesUser.isPresent()) {
                Response response = usersResource.create(kcUser);
                if (response.getStatus() == 201) {

                    //Retrieve the role
                    EesUserRole userRole = eesRoleRepository.findByCode(request.getRoleCode());

                    // Retrieve the Keycloak user ID from the response
                    String kcUserId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                    //update the user to the microservice database
                    eesUser.get().setKeycloakId(kcUserId);
                    eesUser.get().setRole(userRole);
                    eesUser.get().setEnexseEmail(request.getEmail());
                    eesUser.get().setPassword(passwordEncoder.encode(password));
                    eesUser.get().setStatus(EesStatusUser.PENDING);
                    eesUser.get().setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                    eesUserRepository.save(eesUser.get());

                    //asign role after saving user in database because we need keycloakId of the user for the operation
                    assignUserRole(eesUser.get().getUserId(), userRole.getKeycloakRole());

                    userRole.getUsers().add(eesUser.get().getUserId());
                    eesRoleRepository.save(userRole);
                }

                ResponseEntity<Object> emailResponse = mailService.invitationUserEmail(eesUser.get().getPersonalEmail(), password, EesUserConstants.EES_VERIFY_TYPE_INVITATION_USER);
                System.out.println(emailResponse.getStatusCode());
                if (emailResponse.getStatusCode() == HttpStatus.OK) {
                    // L'email a été envoyé avec succès
                    System.out.println("email envoyé");
                    return Response.ok(emailResponse.getBody()).build();
                }
                return response;
            } else {
                throw new Exception("user does not exists in database");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ResponseEntity<AccessTokenResponse> loginUser(EesLoginRequest loginRequest) {
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.getEmail(), loginRequest.getPassword()).build();
        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            LOG.warn("invalid account. User probably hasn't verified email.", ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }
    }

    public boolean assignUserRole(String userId, String roleName) {
        EesUser user;
        if (getUserById(userId).isEmpty()) {
            throw new ObjectFoundException(String.format(EesUserResponse.EES_USER_DOES_NOT_EXISTS));
        } else {
            user = getUserById(userId).get();
            try {
                RoleRepresentation roleRepresentation = kcProvider.getInstance().realm(realm).roles().get(roleName).toRepresentation();
                kcProvider.getInstance().realm(realm).users().get(user.getKeycloakId()).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }


    public Optional<EesUser> getUserById(String id) {
        Optional<EesUser> eesUser = eesUserRepository.findById(id);
        return eesUser;
    }


    public List<String> getAllRolesStartingWithEes() {
        List<RoleRepresentation> allRoles = kcProvider.getInstance().realm(realm).roles().list();

        // Filtrer les noms de rôles pour ne renvoyer que ceux qui commencent par "EES-"
        List<String> filteredRoles = allRoles.stream()
                .map(RoleRepresentation::getName)
                .filter(roleName -> roleName.startsWith("EES-"))
                .collect(Collectors.toList());

        return filteredRoles;
    }
}
