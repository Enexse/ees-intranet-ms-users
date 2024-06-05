package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetWorkplace;
import com.enexse.intranet.ms.users.openfeign.EesWorkplaceService;
import com.enexse.intranet.ms.users.payload.request.*;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesUserTimesheetRequest;
import com.enexse.intranet.ms.users.services.EesUserService;
import com.enexse.intranet.ms.users.services.KeycloakAdminClientService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesUsersController {

    private final KeycloakAdminClientService kcAdminClient;
    private EesUserService eesUserService;

    private EesWorkplaceService eesWorkplaceService;

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_CREATE_USER)
    public ResponseEntity<Object> createUser(
            @RequestBody EesCreateUserRequest request,
            @RequestParam(value = "personalEmail", required = true) String personalEmail) throws Exception {
        ResponseEntity<Object> createdResponse = kcAdminClient.createKeycloakUser(request, personalEmail);
        return ResponseEntity.status(HttpStatus.OK).body(createdResponse.getBody());
    }

    @PostMapping(EesUserEndpoints.EES_LOGIN_USER)
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody EesLoginRequest loginRequest) {
        return kcAdminClient.loginUser(loginRequest);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR})
    @PostMapping(EesUserEndpoints.EES_UPDATE_AVATAR)
    public ResponseEntity<Object> updateAvatar(@PathVariable String userId, @RequestParam("avatar") MultipartFile avatar) {
        return eesUserService.updateAvatar(userId, avatar);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_AVATAR)
    public ResponseEntity<Object> deleteAvatar(@PathVariable String userId) {
        return eesUserService.deleteAvatar(userId);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_INSERT_USER)
    public ResponseEntity<Object> eesInsertUser(@Valid @RequestBody EesUserRequest request) {
        return eesUserService.insertUser(request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR})
    @PutMapping(EesUserEndpoints.EES_UPDATE_PROFIL_USER)
    public ResponseEntity<Object> eesUpdateUserProfile(@PathVariable String userId, @RequestBody EesUpdateUserProfile request) {
        return eesUserService.updateUserProfile(userId, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR})
    @PostMapping(EesUserEndpoints.EES_CERTIFICATE_EMAIL_USER)
    public ResponseEntity<Object> eesCertificateEmailUser(@RequestBody @Valid EesUserEmailRequest request) {
        return eesUserService.certificateEmailUser(request);
    }

    //@RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR}) -> Needs in web without connected
    @PostMapping(EesUserEndpoints.EES_SEND_CODE_TWO_FACTORY_AUTH)
    public ResponseEntity<Object> eesSendCode2FactoryUser(@RequestParam(value = "enexseEmail", required = true) String enexseEmail) {
        return eesUserService.sendCode2FactoryUser(enexseEmail);
    }

    //@RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR}) -> Needs in web without connected
    @PutMapping(EesUserEndpoints.EES_ACTIVATE_OR_DISABLED)
    public ResponseEntity<Object> eesActivateOrDisabledUser(@RequestParam(value = "userId", required = true) String userId,
                                                            @RequestParam(value = "link", required = true) String link) {
        return eesUserService.activateOrDisabledUser(userId, link);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_COLLABORATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_DISABLED_USER)
    public ResponseEntity<Object> eesDisabledUser(@RequestParam(value = "userId", required = true) String userId) {
        return eesUserService.disableUser(userId);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_USER_BY_ID)
    public Optional<EesUser> eesGetUserById(@PathVariable String userId) {
        return eesUserService.getUserById(userId);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_COLLABORATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_USER_BY_ENEXSE_EMAIL)
    public Optional<EesUser> eesGetUserByEnexseEmail(@RequestParam(value = "enexseEmail", required = true) String email) {
        return eesUserService.getUserByEnexseEmail(email);
    }

    //@RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_COLLABORATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_PASSWORD)
    public ResponseEntity<Object> updatePassword(@PathVariable String userId, @RequestBody String newPassword, @RequestParam String idlink) {
        return eesUserService.updatePassword(userId, newPassword, idlink);
    }

    @PutMapping(EesUserEndpoints.EES_UPDATE_LAST_LOGIN)
    public ResponseEntity<Object> updateLastLogin(@PathVariable String userId, @RequestBody String date) {
        return eesUserService.updateLastLogin(userId, date);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_COLLABORATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_CHANGE_PASSWORD)
    public ResponseEntity<Object> eesChangePassword(@PathVariable String userId,
                                                    @RequestParam(value = "currentPassword", required = true) String currentPassword,
                                                    @RequestBody EesChangePasswordRequest request) {
        return eesUserService.changePassword(userId, currentPassword, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_COLLABORATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_CHANGE_PERSONAL_EMAIL)
    public ResponseEntity<Object> eesChangePersonalEmail(@PathVariable String userId,
                                                         @RequestBody EesUserEmailRequest request
    ) throws Exception {
        return eesUserService.changePersonalEmail(userId, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_ALL_USERS_PAGINATED)
    public ResponseEntity<Page<EesUser>> eesGetLastCollaboratorsPaginated(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "5") int pageSize) {
        Page<EesUser> collaborators = eesUserService.getLastCollaboratorsPaginated(page, pageSize);
        return ResponseEntity.ok(collaborators);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_GET_ALL_USERS_FILTRED)
    public ResponseEntity<Page<EesUser>> paginateCollaborators(
            @RequestBody List<String> ids,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize) {

        Page<EesUser> collabs = eesUserService.paginateCollaboratorsFiltred(ids, page, pageSize);
        return ResponseEntity.ok(collabs);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_ALL_USERS)
    public ResponseEntity<Object> eesGetAllCollaborators() {
        return eesUserService.getAllCollaborators();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_USER)
    public ResponseEntity<Object> eesUpdateUser(@PathVariable String userId,
                                                @RequestBody EesUserTimesheetRequest request) {
        return eesUserService.updateUser(userId, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_USER_ASSIGN_ROLE)
    public ResponseEntity<String> assignRole(@PathVariable("userId") String userId,
                                             @RequestParam("roleName") String roleName) {
        boolean success = kcAdminClient.assignUserRole(userId, roleName);

        if (success) {
            return ResponseEntity.ok("Role assigned successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to assign the role.");
        }
    }

    @PostMapping(EesUserEndpoints.EES_USER_IS_TEMPORARY_PASSWORD)
    public boolean isTemporaryPassword(@RequestParam String userId, @RequestParam String password) {
        return eesUserService.checkTemporaryPassword(userId, password);
    }

    @GetMapping(EesUserEndpoints.EES_USER_IS_PASSWORD_EXPIRED)
    public boolean isPasswordExpired(@PathVariable String userId) {
        return eesUserService.checkPasswordExpired(userId);
    }

    @PutMapping(EesUserEndpoints.EES_ACTIVATE_USER)
    public ResponseEntity<Object> activateUser(@RequestParam String userId) {
        return eesUserService.activateUser(userId);
    }

    @PutMapping(EesUserEndpoints.EES_UPDATE_USER_INFO_LANGUAGE)
    public ResponseEntity<Object> eesUpdateUserInfoLanguage(@RequestParam String userId, @RequestParam String language) {
        return eesUserService.updateUserInfoLanguage(userId, language);
    }

    @PutMapping(EesUserEndpoints.EES_UPDATE_USER_INFO_2FACTORY_AUTH)
    public ResponseEntity<Object> eesUpdateUserInfoAuth2Factory(@RequestParam String userId) {
        return eesUserService.updateUserInfoAuth2Factory(userId);
    }

    @PostMapping(EesUserEndpoints.EES_VERIFY_CODE_AUTH)
    public ResponseEntity<Object> eesVerificationCodeAuth(
            @RequestParam(value = "enexseEmail", required = true) String enexseEmail,
            @RequestParam(value = "code", required = true) String code) {
        return eesUserService.verificationCodeAuth(enexseEmail, code);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_COLLABORATOR_INFO)
    public ResponseEntity<Object> eesUpdateCollaboratorInfo(
            @PathVariable String userId,
            @RequestBody EesUpdateCollaboratorInfoRequest request) {
        return eesUserService.updateCollaboratorInfo(userId, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_COLLABORATOR_STATUS)
    public ResponseEntity<Object> eesUpdateCollaboratorStatus(
            @PathVariable String userId,
            @RequestParam(value = "status", required = true) String status) {
        return eesUserService.updateCollaboratorStatus(userId, status);
    }

    @RolesAllowed({ EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR })
    @GetMapping(EesUserEndpoints.EES_GET_ENTITY)
    public ResponseEntity<Object> getEntity(@PathVariable String userId) {
        return eesUserService.getEntity(userId);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_DEPARTMENT)
    public ResponseEntity<Object> getDepartment(@PathVariable String userId) {
        return eesUserService.getDepartment(userId);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_ROLE)
    public ResponseEntity<Object> getRole(@PathVariable String userId) {
        return eesUserService.getRole(userId);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR})
    @GetMapping(EesUserEndpoints.EES_GET_PROFESSION)
    public ResponseEntity<Object> getProfession(@PathVariable String userId) {
        return eesUserService.getProfession(userId);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_LAST_USERS)
    public List<EesUser> getLastThreeUsers() {
        return eesUserService.getLastUsers();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_ADD_MISSION_TO_COLLABORATOR)
    public ResponseEntity<Object> addMissionToCollaborator(
            @PathVariable("userId") String userId,
            @RequestBody EesMissionRequest request
    ) {
        ResponseEntity<Object> response = eesUserService.addMissionToCollaborator(userId, request);
        return response;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_ADD_FORMATION_TO_COLLABORATOR)
    public ResponseEntity<Object> addFormationToCollaborator(
            @PathVariable("userId") String userId,
            @RequestBody EesFormationRequest request
    ) {
        ResponseEntity<Object> response = eesUserService.addFormationToCollaborator(userId, request);
        return response;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_MISSION_FROM_COLLABORATOR)
    public ResponseEntity<Object> preventMissionFromCollaborator(
            @PathVariable("userId") String userId,
            @RequestParam("missionId") String missionId
    ) {
        ResponseEntity<Object> response = eesUserService.preventMissionFromCollaborator(userId, missionId);
        return response;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_FORMATION_FROM_COLLABORATOR)
    public ResponseEntity<Object> preventFormationFromCollaborator(
            @PathVariable("userId") String userId,
            @RequestParam("formationId") String formationId
    ) {
        ResponseEntity<Object> response = eesUserService.preventFormationFromCollaborator(userId, formationId);
        return response;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_MISSION_COLLABORATOR)
    public ResponseEntity<Object> updateMission(
            @PathVariable("userId") String userId,
            @RequestParam("missionId") String missionId,
            @RequestBody EesMissionRequest request
    ) {
        ResponseEntity<Object> response = eesUserService.updateMission(userId, missionId, request);
        return response;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_ADD_COMMENT_MISSION_COLLABORATOR)
    public ResponseEntity<Object> addCommentCareer(
            @PathVariable("userId") String userId,
            @RequestParam("careerId") String careerId,
            @RequestParam("comment") String comment
    ) {
        ResponseEntity<Object> response = eesUserService.addCommentCareer(userId, careerId, comment);
        return response;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_FORMATION_COLLABORATOR)
    public ResponseEntity<Object> updateFormation(
            @PathVariable("userId") String userId,
            @RequestParam("formationId") String missionId,
            @RequestBody EesFormationRequest request
    ) {
        ResponseEntity<Object> response = eesUserService.updateFormation(userId, missionId, request);
        return response;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR})
    @GetMapping(EesUserEndpoints.EES_GET_MISSIONS_FROM_COLLABORTOR)
    public ResponseEntity<Object> retrieveMissionsFromCollaborator(@PathVariable("userId") String userId) {
        ResponseEntity<Object> response = eesUserService.retrieveMissionsFromCollaborator(userId);
        return response;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR})
    @GetMapping(EesUserEndpoints.EES_GET_FORMATIONS_FROM_COLLABORTOR)
    public ResponseEntity<Object> retrieveFormationsFromCollaborator(
            @PathVariable("userId") String userId
    ) {
        ResponseEntity<Object> response = eesUserService.retrieveFormationsFromCollaborator(userId);
        return response;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR})
    @PutMapping(EesUserEndpoints.EES_ARCHIVE_USER)
    public ResponseEntity<Object> archiveUser(@PathVariable String userId) {
        ResponseEntity<Object> response = eesUserService.archiveUser(userId);
        return response;
    }

    @GetMapping("/user/getworkplace")
    public EesTimeSheetWorkplace getWorkplace(@RequestParam String workPlaceCode) {
        EesTimeSheetWorkplace workplace = eesWorkplaceService.findByCode(workPlaceCode);
        return workplace;
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_RESEND_INVITATION_USER)
    public ResponseEntity<Object> eesResendInvitationUser(
            @RequestBody EesCreateUserRequest request,
            @RequestParam(value = "personalEmail", required = true) String personalEmail) {
        ResponseEntity<Object> response = eesUserService.resendInvitationUser(request, personalEmail);
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }
}
