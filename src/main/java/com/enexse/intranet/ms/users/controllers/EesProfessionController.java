package com.enexse.intranet.ms.users.controllers;


import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesUserProfession;
import com.enexse.intranet.ms.users.payload.request.EesProfessionRequest;
import com.enexse.intranet.ms.users.services.EesProfessionService;
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
public class EesProfessionController {

    private EesProfessionService professionService;

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_INSERT_PROFESSION)
    public ResponseEntity<Object> eesInsertProfession(@Valid @RequestBody EesProfessionRequest request) {
        return professionService.insertProfession(request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_All_PROFESSIONS)
    public List<EesUserProfession> eesGetAllProfessions() {
        return professionService.getAllProfessions();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_COLLABORATOR, EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_PROFESSION_BY_CODE)
    public EesUserProfession eesGetprofessionByCode(@PathVariable String professionCode) {
        return professionService.getProfessionByCode(professionCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_PROFESSION_BY__DEPARTMENT)
    public ResponseEntity<Object> eesGetDepartmentByCodeDepartment(@PathVariable String departmentCode) {
        return professionService.getAllProfessionsByDepartment(departmentCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_PROFESSION)
    public HttpEntity<Object> eesUpdateProfessionByCode(@PathVariable String professionCode, @RequestBody EesProfessionRequest request) {
        return professionService.updateProfession(professionCode, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_PROFESSION)
    public ResponseEntity<Object> eesDeleteProfessionByCode(@PathVariable String professionCode) {
        return professionService.deleteProfessionByCode(professionCode);
    }
}
