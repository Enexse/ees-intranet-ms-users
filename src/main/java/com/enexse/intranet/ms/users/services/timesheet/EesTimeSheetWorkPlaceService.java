package com.enexse.intranet.ms.users.services.timesheet;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetWorkplace;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesTimeSheetWorkPlaceRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetWorkPlaceRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class EesTimeSheetWorkPlaceService {
    private EesTimeSheetWorkPlaceRepository workPlaceRepository;

   /* public ResponseEntity<Object> insertTimeSheetWorkPlace(EesTimeSheetWorkPlaceRequest workPlaceRequest) {
        String workPlaceCode = workPlaceRequest.getWorkPlaceCode();

        if (workPlaceCode == null || workPlaceCode.trim().isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_WORKPLACE_CODE_COULD_NOT_BE_NULL), HttpStatus.BAD_REQUEST);
        }

        if (!workPlaceCode.startsWith(EesUserResponse.EES_TIMESHEET_WORKPLACE_PREFIX)) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_WORKPLACE_CODE_INVALID_FORMAT), HttpStatus.BAD_REQUEST);
        }

        if (workPlaceRepository.existsByWorkPlaceCode(workPlaceCode)) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_TIMESHEET_WORKPLACE_ALREADY_EXISTS, workPlaceRequest.getWorkPlaceCode())), HttpStatus.BAD_REQUEST);
        }

        EesTimeSheetWorkplace workplace = EesTimeSheetWorkplace
                .builder()
                .workPlaceCode(workPlaceRequest.getWorkPlaceCode())
                .workPlaceDesignation(workPlaceRequest.getWorkPlaceDesignation())
                .workPlaceObservation(workPlaceRequest.getWorkPlaceObservation())
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .build();
        workPlaceRepository.save(workplace);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_WORKPLACE_CREATED), HttpStatus.CREATED);
    }*/

    public ResponseEntity<Object> updateTimeSheetWorkPlaceByCode(String workPlaceCode, EesTimeSheetWorkPlaceRequest workPlaceRequest) {
        if (!workPlaceCode.startsWith(EesUserResponse.EES_TIMESHEET_WORKPLACE_PREFIX)) {
            workPlaceCode = EesUserResponse.EES_TIMESHEET_WORKPLACE_PREFIX + workPlaceCode;
        }
        EesTimeSheetWorkplace workplace = workPlaceRepository.findByWorkPlaceCode(workPlaceCode);
        System.out.println("++++" + workplace);
        if (workplace == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_WORKPLACE_NOT_FOUND + "" + workPlaceCode), HttpStatus.NOT_FOUND);
        } else {
            if (!workPlaceRequest.getWorkPlaceCode().equals(workPlaceCode)) {
                EesTimeSheetWorkplace existingWorkPlace = workPlaceRepository.findByWorkPlaceCode(workPlaceRequest.getWorkPlaceCode().toUpperCase(Locale.ROOT));
                if (existingWorkPlace != null) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_TIMESHEET_WORKPLACE_ALREADY_EXISTS, workPlaceRequest.getWorkPlaceCode())), HttpStatus.BAD_REQUEST);
                }
            }
        }
        workplace.setWorkPlaceCode(workPlaceRequest.getWorkPlaceCode());
        workplace.setWorkPlaceObservation(workPlaceRequest.getWorkPlaceObservation());
        workplace.setWorkPlaceDesignation(workPlaceRequest.getWorkPlaceDesignation());
        workplace.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
        workPlaceRepository.save(workplace);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_CODE_TIMESHEET_WORKPLACE), HttpStatus.OK);
    }

    public List<EesTimeSheetWorkplace> getAllTimeSheetWorkPlaces() {
        List<EesTimeSheetWorkplace> workplaces = workPlaceRepository.findAll();
        return workplaces;
    }

    public EesTimeSheetWorkplace getTimeSheetWorkPlaceByCode(String workPlaceCode) {
        if (!workPlaceCode.startsWith(EesUserResponse.EES_TIMESHEET_WORKPLACE_PREFIX)) {
            workPlaceCode = EesUserResponse.EES_TIMESHEET_WORKPLACE_PREFIX + workPlaceCode;
        }
        if ((workPlaceRepository.findByWorkPlaceCode(workPlaceCode)) == null) {
            return null;
        } else {
            return workPlaceRepository.findByWorkPlaceCode(workPlaceCode);
        }
    }

    public ResponseEntity<Object> deleteTimeSheetWorkPlaceByCode(String workPlaceCode) {
        EesTimeSheetWorkplace workplace = workPlaceRepository.findByWorkPlaceCode(workPlaceCode);
        if (workplace == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_WORKPLACE_NOT_FOUND), HttpStatus.NOT_FOUND);
        } else {
            workPlaceRepository.delete(workplace);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_WORKPLACE_DELETED), HttpStatus.OK);
        }
    }
}
