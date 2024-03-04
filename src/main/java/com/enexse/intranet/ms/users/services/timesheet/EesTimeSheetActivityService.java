package com.enexse.intranet.ms.users.services.timesheet;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetActivity;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesTimeSheetActivityRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetActivityRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class EesTimeSheetActivityService {

    private EesTimeSheetActivityRepository activityRepository;

    public ResponseEntity<Object> insertTimeSheetActivity(EesTimeSheetActivityRequest request) {
        String activityCode = request.getActivityCode();

        if (activityCode == null || activityCode.trim().isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_ACTIVITY_CODE_COULD_NOT_BE_NULL), HttpStatus.BAD_REQUEST);
        }

        if (!activityCode.startsWith(EesUserResponse.EES_TIMESHEET_ACTIVITY_PREFIX)) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_ACTIVITY_CODE_INVALID_FORMAT), HttpStatus.BAD_REQUEST);
        }

        if (activityRepository.existsByActivityCode(activityCode)) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_TIMESHEET_ACTIVITY_ALREADY_EXISTS, request.getActivityCode())), HttpStatus.BAD_REQUEST);
        }

        EesTimeSheetActivity activity = EesTimeSheetActivity
                .builder()
                .activityCode(request.getActivityCode())
                .activityDesignation(request.getActivityDesignation())
                .activityObservation(request.getActivityObservation())
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .build();
        activityRepository.save(activity);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_ACTIVITY_CREATED), HttpStatus.CREATED);
    }

    public List<EesTimeSheetActivity> getAllTimeSheetActivities() {
        List<EesTimeSheetActivity> activities = activityRepository.findAll();
        return activities;
    }

    public ResponseEntity<Object> updateTimeSheetActivityByCode(String activityCode, EesTimeSheetActivityRequest request) {
        if (!activityCode.startsWith(EesUserResponse.EES_TIMESHEET_ACTIVITY_PREFIX)) {
            activityCode = EesUserResponse.EES_TIMESHEET_ACTIVITY_PREFIX + activityCode;
        }
        EesTimeSheetActivity activity = activityRepository.findByActivityCode(activityCode);
        if (activity == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_ACTIVITY_NOT_FOUND + "" + activityCode), HttpStatus.NOT_FOUND);
        } else {
            if (!request.getActivityCode().equals(activityCode)) {
                EesTimeSheetActivity existingActivity = activityRepository.findByActivityCode(request.getActivityCode().toUpperCase(Locale.ROOT));
                if (existingActivity != null) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_TIMESHEET_ACTIVITY_ALREADY_EXISTS, request.getActivityCode())), HttpStatus.BAD_REQUEST);
                }
            }
        }
        activity.setActivityCode(request.getActivityCode());
        activity.setActivityObservation(request.getActivityObservation());
        activity.setActivityDesignation(request.getActivityDesignation());
        activity.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
        activityRepository.save(activity);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_CODE_TIMESHEET_ACTIVITY), HttpStatus.OK);
    }

    public EesTimeSheetActivity getTimeSheetActivityByCode(String activityCode) {
        if (!activityCode.startsWith(EesUserResponse.EES_TIMESHEET_ACTIVITY_PREFIX)) {
            activityCode = EesUserResponse.EES_TIMESHEET_ACTIVITY_PREFIX + activityCode;
        }
        if ((activityRepository.findByActivityCode(activityCode)) == null) {
            return null;
        } else {
            return activityRepository.findByActivityCode(activityCode);
        }
    }

    public ResponseEntity<Object> deleteTimeSheetActivityByCode(String activityCode) {
        EesTimeSheetActivity activity = activityRepository.findByActivityCode(activityCode);
        if (activity == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_ACTIVITY_NOT_FOUND), HttpStatus.NOT_FOUND);
        } else {
            activityRepository.delete(activity);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_ACTIVITY_DELETED), HttpStatus.OK);
        }
    }
}
