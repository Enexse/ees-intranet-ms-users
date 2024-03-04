package com.enexse.intranet.ms.users.services.timesheet;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetActivity;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetContractHours;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesTimeSheetActivityRequest;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesTimeSheetContractHoursRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetActivityRepository;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetContractHoursRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EesTimeSheetContractHoursService {

    private static char rndChar() {
        int rnd = (int) (Math.random() * 26);
        return (char) ('A' + rnd);
    }


    private EesTimeSheetContractHoursRepository contractHoursRepository;

    public ResponseEntity<Object> insertContractHours(EesTimeSheetContractHoursRequest request) {
        String contractHoursCode = EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_PREFIX;
        char ch1 = rndChar();
        char ch2 = rndChar();
        char ch3 = rndChar();
        contractHoursCode+=ch1;
        contractHoursCode+=ch2;
        contractHoursCode+=ch3;

        if (contractHoursCode == null || contractHoursCode.trim().isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_ACTIVITY_CODE_COULD_NOT_BE_NULL), HttpStatus.BAD_REQUEST);
        }

        if (!contractHoursCode.startsWith(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_PREFIX)) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_CODE_INVALID_FORMAT), HttpStatus.BAD_REQUEST);
        }

        if (contractHoursRepository.existsByContractHoursCode(contractHoursCode)) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_CODE_ALREADY_EXISTS, contractHoursCode)), HttpStatus.BAD_REQUEST);
        }

        Optional<EesTimeSheetContractHours> eesTimeSheetContractHours = null;
        eesTimeSheetContractHours = contractHoursRepository.findByContractHours(request.getContractHours());
        if(eesTimeSheetContractHours.isPresent()){
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_ALREADY_EXISTS, request.getContractHours())), HttpStatus.BAD_REQUEST);
        }

        EesTimeSheetContractHours contractHours = EesTimeSheetContractHours
                .builder()
                .contractHoursCode(contractHoursCode)
                .contractHours(request.getContractHours())
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .build();
        contractHoursRepository.save(contractHours);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_CREATED), HttpStatus.CREATED);
    }

    public List<EesTimeSheetContractHours> getAllTimeSheetContractHours() {
        List<EesTimeSheetContractHours> contractHours = contractHoursRepository.findAll();
        return contractHours;
    }

    public ResponseEntity<Object> updateTimeSheetContractHoursByCode(String contractHoursCode, EesTimeSheetContractHoursRequest request) {
        if (!contractHoursCode.startsWith(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_PREFIX)) {
            contractHoursCode = EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_PREFIX + contractHoursCode;
        }
        EesTimeSheetContractHours contractHours = contractHoursRepository.findByContractHoursCode(contractHoursCode);
        if (contractHoursCode == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_NOT_FOUND + "" + contractHoursCode), HttpStatus.NOT_FOUND);
        }
        EesTimeSheetContractHours contract = getTimeSheetContractHoursByCode(contractHoursCode);
        if(Objects.equals(contract.getContractHours(), request.getContractHours())){
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_NOTHING_TO_UPDATE)), HttpStatus.BAD_REQUEST);
        }
        Optional<EesTimeSheetContractHours> eesTimeSheetContractHours = null;
        eesTimeSheetContractHours = contractHoursRepository.findByContractHours(request.getContractHours());
        if(eesTimeSheetContractHours.isPresent()){
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_ALREADY_EXISTS, request.getContractHours())), HttpStatus.BAD_REQUEST);
        }
        contractHours.setContractHours(request.getContractHours());
        contractHours.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
        contractHoursRepository.save(contractHours);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_CODE_TIMESHEET_CONTRACTHOURS), HttpStatus.OK);

    }

    public EesTimeSheetContractHours getTimeSheetContractHoursByCode(String contractHoursCode) {
        if (!contractHoursCode.startsWith(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_PREFIX)) {
            contractHoursCode = EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_PREFIX + contractHoursCode;
        }
        if ((contractHoursRepository.findByContractHoursCode(contractHoursCode)) == null) {
            return null;
        } else {
            return contractHoursRepository.findByContractHoursCode(contractHoursCode);
        }
    }

    public ResponseEntity<Object> deleteTimeSheetContractHoursByCode(String contractHoursCode) {
        EesTimeSheetContractHours contractHours = contractHoursRepository.findByContractHoursCode(contractHoursCode);
        if (contractHours == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_NOT_FOUND), HttpStatus.NOT_FOUND);
        } else {
            contractHoursRepository.delete(contractHours);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_TIMESHEET_CONTRACTHOURS_DELETED), HttpStatus.OK);
        }
    }


}
