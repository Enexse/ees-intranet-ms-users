package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesContract;
import com.enexse.intranet.ms.users.models.EesCustomer;
import com.enexse.intranet.ms.users.payload.request.EesContractRequest;
import com.enexse.intranet.ms.users.services.EesContractService;
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
public class EesContractController {

    private EesContractService eesContractService;

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PostMapping(EesUserEndpoints.EES_INSERT_CONTRACT)
    public ResponseEntity<Object> eesInsertContract(@Valid @RequestBody EesContractRequest request) {
        return eesContractService.insertContract(request);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_All_CONTRACTS)
    public List<EesContract> eesGetAllContracts() {
        return eesContractService.getAllContracts();
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_CONTRACT_BY_CODE)
    public ResponseEntity<Object> eesGetContractByCode(@PathVariable String contractCode) {
        return eesContractService.getContractByCode(contractCode);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_UPDATE_CONTRACT_BY_CODE)
    public ResponseEntity<Object> eesUpdateContractByCode(@PathVariable String contractCode,
                                                          @RequestBody EesContractRequest request) {
        return eesContractService.updateContractByCode(contractCode, request);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_ACTIVATE_DISABLED_CONTRACT_BY_CODE)
    public ResponseEntity<Object> eesChangeStatusContract(@PathVariable String contractCode,
                                                            @RequestParam(value = "status", required = true) String status) {
        return eesContractService.changeStatusContract(contractCode, status);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @DeleteMapping(EesUserEndpoints.EES_DELETE_CONTRACT_BY_CODE)
    public ResponseEntity<Object> eesDeleteContractByCode(@PathVariable String contractCode) {
        return eesContractService.deleteContractByCode(contractCode);
    }
}
