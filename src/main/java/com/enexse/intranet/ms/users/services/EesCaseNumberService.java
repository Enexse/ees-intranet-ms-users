package com.enexse.intranet.ms.users.services;


import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesCaseNumber;
import com.enexse.intranet.ms.users.models.EesCustomer;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.payload.request.EesCaseNumberRequest;
import com.enexse.intranet.ms.users.payload.response.EesGenericResponse;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesCaseNumberRepository;
import com.enexse.intranet.ms.users.repositories.EesCustomerRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EesCaseNumberService {

    private EesUserRepository eesUserRepository;
    private EesCustomerRepository eesCustomerRepository;
    private EesCaseNumberRepository eesCaseNumberRepository;

    public ResponseEntity<Object> insertCaseNumber(EesCaseNumberRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        Optional<EesCustomer> customer = null;
        customer = eesCustomerRepository.findById(request.getCustomerId());

        if (customer.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CUSTOMER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (request.getCode().length() != 13) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CASE_NUMBER_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }

        EesCaseNumber existingNumber = eesCaseNumberRepository.findByCode(request.getCode().toUpperCase(Locale.ROOT));

        if (existingNumber != null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CASE_NUMBER_ALREADY_EXISTS, request.getCode())), HttpStatus.BAD_REQUEST);
        } else {
            EesCaseNumber caseNumber = new EesCaseNumber()
                    .builder()
                    .code(request.getCode().toUpperCase(Locale.ROOT))
                    .customer(customer.get())
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .createdBy(user.get())
                    .build();
            eesCaseNumberRepository.save(caseNumber);
            return new ResponseEntity<Object>(new EesGenericResponse(caseNumber, EesUserResponse.EES_CASE_NUMBER_CREATED), HttpStatus.CREATED);
        }
    }

    public List<EesCaseNumber> getAllCaseNumbers() {
        List<EesCaseNumber> caseNumbers = eesCaseNumberRepository.findAll();
        return caseNumbers;
    }

    public ResponseEntity<Object> getCaseNumberCode(String code) {

        EesCaseNumber caseNumber = eesCaseNumberRepository.findByCode(code);
        if (caseNumber == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CASE_NUMBER_NOT_FOUND, code)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(caseNumber, HttpStatus.OK);
    }


    public ResponseEntity<Object> updateCaseNumberByCode(String code, EesCaseNumberRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        Optional<EesCustomer> customer = null;
        customer = eesCustomerRepository.findById(request.getCustomerId());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        EesCaseNumber caseNumber = eesCaseNumberRepository.findByCode(code);
        if (caseNumber == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CASE_NUMBER_NOT_FOUND, code)), HttpStatus.NOT_FOUND);
        } else {
            if (request.getCode().length() != 13) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CASE_NUMBER_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {
                if (!(caseNumber.getCode().equals(request.getCode()))) {
                    EesCaseNumber existingNumber = eesCaseNumberRepository.findByCode(request.getCode());
                    if (existingNumber != null) {
                        return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CASE_NUMBER_ALREADY_EXISTS, request.getCode())), HttpStatus.BAD_REQUEST);
                    }
                }
                caseNumber.setCode(request.getCode());
                caseNumber.setCustomer(customer.get());
                caseNumber.setCreatedBy(user.get());
                caseNumber.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                eesCaseNumberRepository.save(caseNumber);
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CASE_NUMBER_UPDATED), HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> deleteCaseNumberByCode(String code) {
        EesCaseNumber caseNumber = eesCaseNumberRepository.findByCode(code);

        if (caseNumber != null) {
            eesCaseNumberRepository.delete(caseNumber);
            return new ResponseEntity<>(new EesMessageResponse(EesUserResponse.EES_CASE_NUMBER_DELETED), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
