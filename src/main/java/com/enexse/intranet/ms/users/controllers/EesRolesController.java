package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.enums.EesKeycloakRoles;
import com.enexse.intranet.ms.users.models.EesUserRole;
import com.enexse.intranet.ms.users.payload.request.EesRoleRequest;
import com.enexse.intranet.ms.users.services.EesRoleService;
import com.enexse.intranet.ms.users.services.KeycloakAdminClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesRolesController {

    public static final String ADMIN_ROLE = EesKeycloakRoles.ADMIN.getRole();
    private EesRoleService eesRoleService;
    @Autowired
    private KeycloakAdminClientService keycloakAdminClientService;

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PostMapping(EesUserEndpoints.EES_INSERT_ROLE)
    public ResponseEntity<Object> eesInsertRole(@Valid @RequestBody EesRoleRequest request) {
        return eesRoleService.insertRole(request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_All_ROLES)
    public List<EesUserRole> eesGetAllRoles() {
        return eesRoleService.getAllRoles();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_ROLE_BY_CODE_ROLE)
    public EesUserRole eesGetRoleByCodeRole(@RequestParam String roleCode) {
        return eesRoleService.getRoleByCode(roleCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_ROLE_BY_TITLE)
    public EesUserRole eesGetRoleByTitle(@RequestParam String roleTitle) {
        return eesRoleService.getRoleByTitle(roleTitle);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_ROLE)
    public HttpEntity<Object> eesUpdateRoleByCode(@PathVariable String roleCode, @RequestBody EesRoleRequest request) {
        return eesRoleService.updateRoleByCode(roleCode, request);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_ROLE)
    public ResponseEntity<Object> eesDeleteRoleByCode(@PathVariable String roleCode) {
        return eesRoleService.deleteRoleByCode(roleCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_PERMISSIONS_BY_CODE_ROLE)
    public ResponseEntity<Object> getListOfPermissionsByRoleCode(@PathVariable String roleCode) {
        return eesRoleService.getListOfPermissionsByRoleCode(roleCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_USERS_BY_ROLE)
    public ResponseEntity<Object> getUsersByRole(@PathVariable String roleCode) {
        return eesRoleService.usersByRole(roleCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_All_KEYCLOACK_ROLES)
    public List<String> getRoles() {
        // Appeler le service pour obtenir les rôles commençant par "EES-"
        return keycloakAdminClientService.getAllRolesStartingWithEes();
    }
}

