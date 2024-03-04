package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesCaseNumber;
import com.enexse.intranet.ms.users.payload.request.EesCaseNumberRequest;
import com.enexse.intranet.ms.users.services.EesCaseNumberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesCaseNumberController {

    private EesCaseNumberService eesCaseNumberService;

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_INSERT_CASE_NUMBER)
    public ResponseEntity<Object> eesInsertCaseNumber(@Valid @RequestBody EesCaseNumberRequest request) {
        return eesCaseNumberService.insertCaseNumber(request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_All_CASE_NUMBERS)
    public List<EesCaseNumber> eesGetAllCaseNumbers() {
        return eesCaseNumberService.getAllCaseNumbers();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_CASE_NUMBER_BY_CODE)
    public ResponseEntity<Object> eesGetCaseNumber(@PathVariable String code) {
        return eesCaseNumberService.getCaseNumberCode(code);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_CASE_NUMBER_BY_CODE)
    public ResponseEntity<Object> eesUpdateContractByCode(@PathVariable String code,
                                                          @RequestBody EesCaseNumberRequest request) {
        return eesCaseNumberService.updateCaseNumberByCode(code, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_CASE_NUMBER_BY_CODE)
    public ResponseEntity<Object> eesDeleteContractByCode(@PathVariable String code) {
        return eesCaseNumberService.deleteCaseNumberByCode(code);
    }
}
