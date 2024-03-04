package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesGroup;
import com.enexse.intranet.ms.users.payload.request.EesCollaboratorEmailRequest;
import com.enexse.intranet.ms.users.payload.request.EesGroupEmailRequest;
import com.enexse.intranet.ms.users.payload.request.EesGroupRequest;
import com.enexse.intranet.ms.users.services.EesGroupService;
import com.enexse.intranet.ms.users.services.EesMailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesGroupsController {

    private EesGroupService eesGroupService;
    private EesMailService eesMailService;

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_INSERT_GROUP)
    public ResponseEntity<Object> eesInsertUser(@Valid @RequestBody EesGroupRequest request) {
        return eesGroupService.insertGroup(request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_ALL_GROUPS)
    public ResponseEntity<Object> eesGetAllGroups() {
        return eesGroupService.getAllGroups();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_GROUP)
    public ResponseEntity<Object> eesDeleteGroupByCode(@PathVariable String groupCode) {
        return eesGroupService.deleteGroup(groupCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_GROUP_BY_CODE)
    public Optional<EesGroup> eesGetGroupByCode(@PathVariable String groupCode) {
        return eesGroupService.getGroupByCode(groupCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_GROUP_BY_CODE)
    public ResponseEntity<Object> eesUpdateGroupByCode(@PathVariable String groupCode, @RequestBody EesGroupRequest request) {
        return eesGroupService.updateGroupByCode(groupCode, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_SEND_MAIL_TO_GROUP)
    public ResponseEntity<Object> eesSendMailToGroup(@RequestPart("request") String requestJson,
                                                     @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) {
        try {
            // Parse the JSON string into EesGroupEmailRequest object
            ObjectMapper objectMapper = new ObjectMapper();
            EesGroupEmailRequest request = objectMapper.readValue(requestJson, EesGroupEmailRequest.class);

            // Call the service method with the parsed JSON object and the attachment
            return eesMailService.sendEmailToGroups(request, attachments);
        } catch (Exception ex) {
            return new ResponseEntity<Object>("Failed to process the request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_SEND_MAIL_TO_Collaborator)
    public ResponseEntity<Object> eesSendMailToCollaborator(@RequestPart("request") String requestJson,
                                                            @RequestPart(value = "attachment", required = false) List<MultipartFile> attachments) {
        try {
            // Parse the JSON string into EesCollaboratorEmailRequest object
            ObjectMapper objectMapper = new ObjectMapper();
            EesCollaboratorEmailRequest request = objectMapper.readValue(requestJson, EesCollaboratorEmailRequest.class);

            // Call the service method with the parsed JSON object and the attachment
            return eesMailService.sendEmailToCollaborator(request, attachments);
        } catch (Exception ex) {
            return new ResponseEntity<Object>("Failed to process the request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
