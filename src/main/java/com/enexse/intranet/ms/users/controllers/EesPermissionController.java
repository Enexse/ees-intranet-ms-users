package com.enexse.intranet.ms.users.controllers;


import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesUserPermission;
import com.enexse.intranet.ms.users.payload.request.EesPermissionRequest;
import com.enexse.intranet.ms.users.services.EesUserPermissionService;
import lombok.AllArgsConstructor;
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
public class EesPermissionController {

    private EesUserPermissionService permissionService;

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PostMapping(EesUserEndpoints.EES_INSERT_PERMISSION)
    public ResponseEntity<Object> eesInsertPermission(@Valid @RequestBody EesPermissionRequest request) {
        return permissionService.insertPermission(request);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_All_PERMISSIONS)
    public List<EesUserPermission> eesGetAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_PERMISSION_BY_CODE)
    public EesUserPermission eesGetPermissionByCode(@RequestParam String permissionCode) {
        return permissionService.getPermissionsByCode(permissionCode);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_PERMISSION_TITLE)
    public String eesGetPermissionTitle(@RequestParam String permissionCode) {
        return permissionService.getPermissionTitleByCode(permissionCode);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_PERMISSION)
    public ResponseEntity<Object> eesDeletePermissionByCode(@PathVariable String permissionCode) {
        return permissionService.deletePermissionByCode(permissionCode);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_PERMISSION)
    public HttpEntity<Object> eesUpdatePermissionByCode(@PathVariable String permissionCode, @RequestBody EesPermissionRequest request) {
        return permissionService.updatePermissionByCode(permissionCode, request);
    }
}
