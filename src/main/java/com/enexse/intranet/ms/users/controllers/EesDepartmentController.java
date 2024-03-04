package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserDepartment;
import com.enexse.intranet.ms.users.payload.request.EesDepartmentRequest;
import com.enexse.intranet.ms.users.services.EesUserDepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesDepartmentController {

    private EesUserDepartmentService eesUserDepartmentService;

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_INSERT_DEPARTMENT)
    public ResponseEntity<Object> eesInsertDepartment(@Valid @RequestBody EesDepartmentRequest request) {
        return eesUserDepartmentService.insertDepartment(request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_All_DEPARTMENTS)
    public List<EesUserDepartment> eesGetAllDepartments() {
        return eesUserDepartmentService.getAllDepartments();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_DEPARTMENT_BY_CODE_DEPARTMENT)
    public EesUserDepartment eesGetDepartmentByCodeDepartment(@RequestParam String departmentCode) {
        return eesUserDepartmentService.getDepartmentByCode(departmentCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_DEPARTMENT)
    public HttpEntity<Object> eesUpdateDepartmentByCode(@PathVariable String departmentCode, @RequestBody EesDepartmentRequest request) {
        return eesUserDepartmentService.updateDepartmentByCode(departmentCode, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_DEPARTMENT)
    public ResponseEntity<Object> eesDeleteDepartmentByCode(@PathVariable String departmentCode) {
        return eesUserDepartmentService.deleteDepartmentByCode(departmentCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_USERS_BY_DEPARTMENT)
    public Optional<List<EesUser>> eesGetUsersByCode(@PathVariable String departmentCode) {
        return eesUserDepartmentService.getUsersByDepartment(departmentCode);
    }
}
