package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.configurations.keycloakProvider;
import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.enums.EesKeycloakRoles;
import com.enexse.intranet.ms.users.exceptions.ObjectFoundException;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserPermission;
import com.enexse.intranet.ms.users.models.EesUserRole;
import com.enexse.intranet.ms.users.models.EesUserRolePermission;
import com.enexse.intranet.ms.users.payload.request.EesPermissionAccessRequest;
import com.enexse.intranet.ms.users.payload.request.EesRoleRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesRoleRepository;
import com.enexse.intranet.ms.users.repositories.EesUserAccessRepository;
import com.enexse.intranet.ms.users.repositories.EesUserPermissionRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EesRoleService {

    @Value("${keycloak.realm}")
    public String realm;

    @Autowired
    private EesRoleRepository eesRoleRepository;
    @Autowired
    private EesUserPermissionRepository eesPermissionRepository;
    @Autowired
    private EesUserAccessRepository eesAccessRepository;
    @Autowired
    private EesUserRepository eesUserRepository;
    @Autowired
    private keycloakProvider kcProvider;

    public ResponseEntity<Object> insertRole(EesRoleRequest request) {

        if (!request.roleCode.startsWith(EesUserResponse.EES_ROLE_PREFIX) || request.getRoleCode().length() != 12) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ROLE_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }

        EesUserRole existingRole = eesRoleRepository.findByCode(request.getRoleCode().toUpperCase(Locale.ROOT));

        if (existingRole != null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ROLE_ALREADY_EXISTS, request.getRoleCode())), HttpStatus.BAD_REQUEST);
        }

        // Récupération des permissions à partir de leur code
        List<String> permissionCodes = new ArrayList<>();
        List<EesUserPermission> permissions = new ArrayList<>();
        for (EesPermissionAccessRequest permissionAccess : request.getPermissionAccesses()) {
            EesUserPermission permission = eesPermissionRepository.findByPermissionCode(permissionAccess.getPermissionCode());
            if (permission != null) {
                permissions.add(permission);
                permissionCodes.add(permissionAccess.getPermissionCode());
            } else {
                throw new IllegalArgumentException("Permission not found: " + permissionAccess.getPermissionCode());
            }
        }
        EesUserRolePermission access = EesUserRolePermission.builder()
                .permissionCode(permissionCodes)
                .roleCode(request.getRoleCode())
                .build();
        //accesses.add(access);
        eesAccessRepository.save(access);
        System.out.println("role access" + access);

        // Create the EesUserRole object
        EesUserRole role = EesUserRole.builder()
                .roleCode(request.getRoleCode())
                .keycloakRole(request.getKeycloakRole())
                .roleDescription(request.getRoleDescription())
                .roleTitle(request.getRoleTitle())
                .permissions(permissions)
                .permissionAccessRequest(request.getPermissionAccesses())
                .build();

        // Save the EesUserRole object
        eesRoleRepository.save(role);
        System.out.println("saved role" + role);

        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ROLE_CREATED), HttpStatus.CREATED);

    }

    public List<EesUserRole> getAllRoles() {
        List<EesUserRole> roles = eesRoleRepository.findAll()
                .stream().sorted(Comparator.comparing(EesUserRole::getRoleCode).reversed()).collect(Collectors.toList());
        return roles;
    }

    public EesUserRole getRoleByCode(String roleCode) {
        if (!roleCode.startsWith(EesUserResponse.EES_ROLE_PREFIX)) {
            roleCode = EesUserResponse.EES_ROLE_PREFIX + roleCode;
        }
        return eesRoleRepository.findByCode(roleCode);
    }

    public EesUserRole getRoleByTitle(String roleTitle) {
        return eesRoleRepository.findByroleTitle(roleTitle);
    }

    public ResponseEntity<Object> updateRoleByCode(String roleCode, EesRoleRequest request) {
        String prefixRoleCode = EesUserResponse.EES_ROLE_PREFIX + roleCode;
        EesUserRole role = eesRoleRepository.findByCode(prefixRoleCode);
        if (role == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ROLE_NOT_FOUND + roleCode), HttpStatus.NOT_FOUND);
        } else {
            if (request.getRoleCode().length() != 12) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ROLE_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {
                //added an extra check before checking for the duplicate role code.
                if (!request.getRoleCode().equals(prefixRoleCode)) {
                    EesUserRole existingRole = eesRoleRepository.findByCode(request.getRoleCode().toUpperCase(Locale.ROOT));
                    if (existingRole != null) {
                        return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ROLE_ALREADY_EXISTS, request.getRoleCode())), HttpStatus.BAD_REQUEST);
                    }
                }

                // Récupération des permissions à partir de leur code
                List<String> permissionCodes = new ArrayList<>();
                List<EesUserPermission> permissions = new ArrayList<>();

                for (EesPermissionAccessRequest permissionAccess : request.getPermissionAccesses()) {
                    EesUserPermission permission = eesPermissionRepository.findByPermissionCode(permissionAccess.getPermissionCode());
                    if (permission != null) {
                        permissions.add(permission);
                        permissionCodes.add(permissionAccess.getPermissionCode());
                    } else {
                        throw new IllegalArgumentException("Permission not found: " + permissionAccess.getPermissionCode());
                    }
                }

                EesUserRolePermission access = new EesUserRolePermission();
                if (access != null) {
                    access.setRoleCode(request.getRoleCode());
                    access.setPermissionCode(permissionCodes);
                }
                eesAccessRepository.save(access);
                role.setRoleCode(request.getRoleCode());
                role.setKeycloakRole(request.getKeycloakRole());
                role.setRoleDescription(request.getRoleDescription());
                role.setRoleTitle(request.getRoleTitle());
                role.setPermissions(permissions);
                role.setPermissionAccessRequest(request.getPermissionAccesses());
                eesRoleRepository.save(role);
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_CODE_ROLE), HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> getListOfPermissionsByRoleCode(String roleCode) {
        String prefixRoleCode = EesUserResponse.EES_ROLE_PREFIX + roleCode;
        EesUserRole role = eesRoleRepository.findByCode(prefixRoleCode);
        if (role == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ROLE_NOT_FOUND + roleCode), HttpStatus.NOT_FOUND);
        } else {
            List<EesPermissionAccessRequest> permissionsAccessRequest = role.getPermissionAccessRequest();
            return ResponseEntity.ok(permissionsAccessRequest);
        }
    }


    public ResponseEntity<Object> deleteRoleByCode(String roleCode) {

        EesUserRole role = eesRoleRepository.findByCode(EesUserResponse.EES_ROLE_PREFIX + roleCode);
        if (role == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ROLE_NOT_FOUND + roleCode), HttpStatus.NOT_FOUND);
        } else {
            List<String> userIds = null;
            userIds = role.getUsers();
            if (userIds != null) {
                for (String id : userIds) {
                    Optional<EesUser> user = eesUserRepository.findByUserId(id);
                    if (user.isPresent()) {

                        //set field role to null in each user
                        user.get().setRole(null);

                        //retrieve role from user in keycloak(only if the role != EES-COLLABORATOR)
                        if (!Objects.equals(role.getKeycloakRole(), EesKeycloakRoles.COLL.getRole())) {
                            retrieveUserRole(id, role.getKeycloakRole());
                            assignUserRole(id, EesKeycloakRoles.COLL.getRole());
                        }

                        eesUserRepository.save(user.get());
                    }
                }
            }
            eesRoleRepository.delete(role);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ROLE_DELETED), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> usersByRole(String roleCode) {

        if (!roleCode.startsWith(EesUserResponse.EES_ROLE_PREFIX)) {
            roleCode = EesUserResponse.EES_ROLE_PREFIX + roleCode;
        }

        EesUserRole role = eesRoleRepository.findByCode(roleCode);
        if (role != null) {
            List<EesUser> list = new ArrayList<>();

            List<String> users = role.getUsers();
            for (String userId : users) {
                Optional<EesUser> user = eesUserRepository.findByUserId(userId);
                if (user.isPresent()) {
                    list.add(user.get());
                }
            }
            return ResponseEntity.ok(list);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ROLE_NOT_FOUND + roleCode), HttpStatus.NOT_FOUND);
        }
    }

    public boolean retrieveUserRole(String userId, String roleName) {
        EesUser user;
        if (getUserById(userId).isEmpty()) {
            throw new ObjectFoundException(String.format(EesUserResponse.EES_USER_DOES_NOT_EXISTS));
        } else {
            user = getUserById(userId).get();
            try {
                RoleRepresentation roleRepresentation = kcProvider.getInstance().realm(realm).roles().get(roleName).toRepresentation();
                kcProvider.getInstance().realm(realm).users().get(user.getKeycloakId()).roles().realmLevel().remove(Collections.singletonList(roleRepresentation));
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
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


}
