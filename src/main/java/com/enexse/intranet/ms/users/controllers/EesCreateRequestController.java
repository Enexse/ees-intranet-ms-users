package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesUserCreateRequest;
import com.enexse.intranet.ms.users.payload.request.EesCreateRequest;
import com.enexse.intranet.ms.users.payload.request.EesResponseManualRequest;
import com.enexse.intranet.ms.users.services.EesCreateRequestService;
import com.enexse.intranet.ms.users.services.EesSubRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesCreateRequestController {

    private EesCreateRequestService eesCreateRequestService;
    private EesSubRequestService eesSubRequestService;

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_COLLABORATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_CREATE_REQUEST)
    public ResponseEntity<Object> createRequest(@RequestPart("request") String requestJson,
                                                @RequestPart(value = "attachments", required = false) List<MultipartFile> files) {
        try {
            // Parse the JSON string into EesCreateRequest object
            ObjectMapper objectMapper = new ObjectMapper();
            EesCreateRequest request = objectMapper.readValue(requestJson, EesCreateRequest.class);

            return eesCreateRequestService.createRequest(request, files);
        } catch (Exception e) {
            return new ResponseEntity<Object>("Failed to process the request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_COLLABORATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_REQUEST_ATTACHMENT)
    public ResponseEntity<Object> downloadAttachment(@PathVariable String fileId) {
        return eesCreateRequestService.downloadFile(fileId);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_LIST_OF_REQUESTS)
    public ResponseEntity<Object> getListOfRequestsCreated() {
        return eesCreateRequestService.getListOfRequests();
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_LIST_RECIPIENTS)
    public ResponseEntity<Object> getListOfRecipients() {
        return eesCreateRequestService.getListOfRecipients();
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_LIST_OF_REFERENTS)
    public ResponseEntity<Object> getListOfReferents() {
        return eesCreateRequestService.getListOfReferents();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_REQUEST)
    public ResponseEntity<Object> responseRequest(@PathVariable String id, @Valid @RequestBody EesResponseManualRequest request) {
        return eesCreateRequestService.updateRequestStatus(id, request);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_UPDATE_STATUS_REQUEST)
    public ResponseEntity<Object> responseAutomaticRequest(@PathVariable String id) {
        return eesCreateRequestService.updateAutomaticRequestStatus(id);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE, EesUserConstants.EES_ROLE_COLLABORATOR})
    @GetMapping(EesUserEndpoints.EES_GET_REQUESTS_BY_USER)
    public List<EesUserCreateRequest> getAllManualRequestsByUser(@PathVariable String userId) {
        List<EesUserCreateRequest> manualRequests = eesCreateRequestService.getAllManualRequestsByUser(userId);
        return manualRequests.stream().sorted(Comparator.comparing(EesUserCreateRequest::getCreatedAt).reversed()).collect(Collectors.toList());
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_REQUEST)
    public ResponseEntity<Object> getRequest(@PathVariable String id) {
        return eesCreateRequestService.getRequestById(id);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_LAST_REQUESTS)
    public List<EesUserCreateRequest> getLastRequests() {
        return eesCreateRequestService.getLastRequests();
    }

}
