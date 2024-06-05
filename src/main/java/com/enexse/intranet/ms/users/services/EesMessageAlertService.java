package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesMessageAlert;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.payload.request.EesMessageAlertRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesMessageAlertRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EesMessageAlertService {

    private EesMessageAlertRepository alertRepository;
    private EesUserRepository eesUserRepository;

    public ResponseEntity<Object> addAlertMessage(EesMessageAlertRequest alertRequest) {
        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(alertRequest.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, alertRequest.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }
        EesMessageAlert messageAlert = EesMessageAlert
                .builder()
                .alertTitle(alertRequest.getAlertTitle())
                .alertDescription(alertRequest.getAlertDescription())
                .alertStatus(Boolean.FALSE)
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .createdBy(user.get())
                .build();

        alertRepository.save(messageAlert);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_AlERT_CREATED), HttpStatus.CREATED);
    }

    public List<EesMessageAlert> getAlerts() {
        return alertRepository.findAll()
                .stream().sorted(Comparator.comparing(EesMessageAlert::getCreatedAt).reversed()).collect(Collectors.toList());
    }

    public ResponseEntity<Object> getAlertById(String id) {
        Optional<EesMessageAlert> alertMessage = alertRepository.findById(id);
        if (alertMessage.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ALERT_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(alertMessage);
    }

    public ResponseEntity<Object> updateAlert(String id, EesMessageAlertRequest alertRequest) {
        Optional<EesMessageAlert> messageAlertOptional = alertRepository.findById(id);
        if (messageAlertOptional.isPresent()) {
            Optional<EesUser> user = null;
            user = eesUserRepository.findByEnexseEmail(alertRequest.getCreatedBy());

            if (user.isEmpty()) {
                return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, alertRequest.getCreatedBy())), HttpStatus.BAD_REQUEST);
            }
            EesMessageAlert alertMessageRequest = messageAlertOptional.get();
            alertMessageRequest.setAlertTitle(alertRequest.getAlertTitle());
            alertMessageRequest.setAlertDescription(alertRequest.getAlertDescription());
            alertMessageRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            alertMessageRequest.setCreatedBy(user.get());
            alertRepository.save(alertMessageRequest);
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ALERT_UPDATED)), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ALERT_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> changeStatus(String id, boolean newStatus) {
        Optional<EesMessageAlert> messageAlertOptional = alertRepository.findById(id);
        if (messageAlertOptional.isPresent()) {
            EesMessageAlert alertMessage = messageAlertOptional.get();
            boolean previousStatus = alertMessage.isAlertStatus();
            newStatus = previousStatus ? false : true;
            alertMessage.setAlertStatus(newStatus);

            alertRepository.save(alertMessage);
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ALERT_UPDATED)), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ALERT_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> deleteAlert(String id) {
        Optional<EesMessageAlert> messageAlertOptional = alertRepository.findById(id);
        if (messageAlertOptional.isPresent()) {
            EesMessageAlert alertMessage = messageAlertOptional.get();
            alertRepository.delete(alertMessage);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ALERT_DELETED), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ALERT_NOT_FOUND, id)), HttpStatus.NOT_FOUND);
        }
    }
}
