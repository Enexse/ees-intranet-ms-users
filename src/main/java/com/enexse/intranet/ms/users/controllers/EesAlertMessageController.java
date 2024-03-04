package com.enexse.intranet.ms.users.controllers;


import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesMessageAlert;
import com.enexse.intranet.ms.users.payload.request.EesMessageAlertRequest;
import com.enexse.intranet.ms.users.services.EesMessageAlertService;
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
public class EesAlertMessageController {
    private EesMessageAlertService alertService;

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PostMapping(EesUserEndpoints.EES_ALERT_MESSAGE)
    public ResponseEntity<Object> eesInsertAlert(@Valid @RequestBody EesMessageAlertRequest alertRequest) {
        return alertService.addAlertMessage(alertRequest);
    }
    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR,EesUserConstants.EES_ROLE_COLLABORATOR,EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_All_ALERTS)
    public List<EesMessageAlert> getAlerts() {
        return alertService.getAlerts();
    }
    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_ALERT)
    public ResponseEntity<Object> getAlert(@PathVariable String id) {
        return alertService.getAlertById(id);
    }
    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_CHANGE_STATUS_ALERT)
    public ResponseEntity<Object> eesChangeStatusAlert(@PathVariable String id, @RequestParam(value = "alertStatus", required = true) boolean alertStatus) {
        return alertService.changeStatus(id, alertStatus);
    }
    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_UPDATE_ALERT)
    public ResponseEntity<Object> eesUpdateAlertByCode(@PathVariable String id, @RequestBody EesMessageAlertRequest request) {
        return alertService.updateAlert(id, request);
    }
    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @DeleteMapping(EesUserEndpoints.EES_DELETE_ALERT)
    public ResponseEntity<Object> eesDeleteAlertByCode(@PathVariable String id) {
        return alertService.deleteAlert(id);
    }
}
