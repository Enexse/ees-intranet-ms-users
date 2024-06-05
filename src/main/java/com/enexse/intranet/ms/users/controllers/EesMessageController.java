package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesMessage;
import com.enexse.intranet.ms.users.models.EesMessageType;
import com.enexse.intranet.ms.users.payload.request.EesMessageRequest;
import com.enexse.intranet.ms.users.payload.request.EesMessageTypeRequest;
import com.enexse.intranet.ms.users.services.EesMessageService;
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
public class EesMessageController {

    private EesMessageService messageService;

    //@RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES) -> Needs in web without connected
    @PostMapping(EesUserEndpoints.EES_INSERT_MESSAGE)
    public ResponseEntity<Object> eesInsertMessage(@Valid @RequestBody EesMessageRequest messageRequest) {
        return messageService.insertMessage(messageRequest);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_All_MESSAGES)
    public List<EesMessage> eesGetAllMessages() {
        return messageService.getAllMessages();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_INSERT_MESSAGE_TYPE)
    public ResponseEntity<Object> eesInsertMessageType(@Valid @RequestBody EesMessageTypeRequest request) {
        return messageService.insertMessageType(request);
    }

    //@RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES) -> Needs in web without connected
    @GetMapping(EesUserEndpoints.EES_GET_All_MESSAGE_TYPES)
    public List<EesMessageType> eesGetAllMessageTypes() {
        return messageService.getAllMessageTypes();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_MESSAGE_TYPE)
    public HttpEntity<Object> eesUpdateMessageTypeByCode(@PathVariable String code, @RequestBody EesMessageTypeRequest request) {
        return messageService.updateMessageTypeByCode(code, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_MESSAGE_TYPE)
    public ResponseEntity<Object> eesDeleteMessageTypeByCode(@PathVariable String code) {
        return messageService.deleteMessageTypeByCode(code);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_ACTIVATE_MESSAGE_TYPE_BY_CODE)
    public ResponseEntity<Object> eesChangeStatusMessageType(@PathVariable String code,
                                                             @RequestParam(value = "status", required = true) String status) {
        return messageService.changeStatusMessageType(code, status);
    }
}
