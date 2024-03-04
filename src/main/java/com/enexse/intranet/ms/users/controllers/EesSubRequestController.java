package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesUserSubRequest;
import com.enexse.intranet.ms.users.payload.request.EesSubRequest;
import com.enexse.intranet.ms.users.services.EesSubRequestService;
import lombok.AllArgsConstructor;
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
public class EesSubRequestController {

    private EesSubRequestService eesSubRequestService;


    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PostMapping(EesUserEndpoints.EES_INSERT_SUB_REQUEST)
    public ResponseEntity<Object> eesInsertRequest(@Valid @RequestBody EesSubRequest request) {
        return eesSubRequestService.insertSubRequest(request);
    }

   @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_All_SUB_REQUESTS)
    public List<EesUserSubRequest> eesGetAllRequests() {
        return eesSubRequestService.getAllSubRequests();
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_SUB_REQUEST_BY_ID)
    public Optional<EesUserSubRequest> eesGetAllSubRequestsById(@PathVariable String subRequestId) {
        return eesSubRequestService.returnSubRequest(subRequestId);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_SUB_REQUEST_BY_REQUEST_CODE)
    public EesUserSubRequest eesGetSubRequestByCodeRequest(@RequestParam String requestCode) {
        return eesSubRequestService.getSubRequestByCode(requestCode);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_SUB_REQUEST)
    public ResponseEntity<Object> eesUpdateSubRequestByCode(@PathVariable String subrequestCode, @RequestBody EesSubRequest userSubRequest) {
        return eesSubRequestService.updateSubRequest(subrequestCode, userSubRequest);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_SUB_REQUEST)
    public ResponseEntity<Object> eesDeleteSubRequestByCode(@PathVariable String subrequestCode) {
        return eesSubRequestService.deleteSubRequestByCode(subrequestCode);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_LIST_OF_SUB_REQUESTS_BY_CODE)
    public ResponseEntity<Object> getListOfSubRequestsByCode(@RequestParam String requestCode) {
        return eesSubRequestService.getAllSubRequestsByRequestCode(requestCode);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_LIST_OF_SUB_REQUESTS_BY_ID)
    public ResponseEntity<Object> getListOfSubRequestsById(@RequestParam String requestId) {
        return eesSubRequestService.getAllSubRequestsByRequestId(requestId);
    }
}
