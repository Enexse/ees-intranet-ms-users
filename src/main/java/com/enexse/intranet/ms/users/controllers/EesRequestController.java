package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesUserRequest;
import com.enexse.intranet.ms.users.payload.request.EesRequest;
import com.enexse.intranet.ms.users.services.EesRequestService;
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
public class EesRequestController {

    private EesRequestService eesRequestService;

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PostMapping(EesUserEndpoints.EES_INSERT_REQUEST)
    public ResponseEntity<Object> eesInsertRequest(@Valid @RequestBody EesRequest request) {
        return eesRequestService.insertRequest(request);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_REQUEST_BY_ID)
    public Optional<EesUserRequest> eesGetAllSubRequestsById(@PathVariable String requestId) {
        return eesRequestService.returnRequest(requestId);
    }

    @GetMapping(EesUserEndpoints.EES_GET_All_REQUESTS)
    public List<EesUserRequest> eesGetAllRequests() {
        return eesRequestService.getAllRequests();
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_REQUEST_BY_CODE_REQUEST)
    public EesUserRequest eesGetRequestByCodeRequest(@RequestParam String requestCode) {
        return eesRequestService.getRequestByCode(requestCode);
    }

   /* @GetMapping(EesUserEndpoints.EES_GET_REQUEST_BY_ID)
    public Optional<EesUserRequest> eesGetAllRequestsById(@PathVariable String requestId){
        return eesRequestService.getAllRequestsById(requestId);
    }*/

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_REQUEST)
    public HttpEntity<Object> eesUpdateRequestByCode(@PathVariable String requestCode, @RequestBody EesRequest request) {
        return eesRequestService.updateRequestByCode(requestCode, request);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_REQUEST)
    public ResponseEntity<Object> eesDeleteRequestByCode(@PathVariable String requestCode) {
        return eesRequestService.deleteRequestByCode(requestCode);
    }
}
