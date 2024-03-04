package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.enums.EesStatusCustomer;
import com.enexse.intranet.ms.users.models.EesContract;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.payload.request.EesContractRequest;
import com.enexse.intranet.ms.users.payload.response.EesGenericResponse;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesContractRepository;
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
public class EesContractService {

    private EesContractRepository eesContractRepository;
    private EesUserRepository eesUserRepository ;

    public ResponseEntity<Object> insertContract(EesContractRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if(user.isEmpty()){
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (!request.getContractCode().startsWith(EesUserResponse.EES_CONTRACT_PREFIX) || request.getContractCode().length() != 16) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CONTRACT_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }

        EesContract existingContract = eesContractRepository.findByContractCode(request.getContractCode().toUpperCase(Locale.ROOT));

        if (existingContract != null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CONTRACT_ALREADY_EXISTS, request.getContractCode())), HttpStatus.BAD_REQUEST);
        } else {
            EesContract contract = new EesContract()
                    .builder()
                    .contractCode(request.getContractCode().toUpperCase(Locale.ROOT))
                    .contractTitle(request.getContractTitle())
                    .contractDescription(request.getContractDescription())
                    .status(EesStatusCustomer.ACTIVE)
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .createdBy(user.get())
                    .build();
            eesContractRepository.save(contract);
            return new ResponseEntity<Object>(new EesGenericResponse(contract, EesUserResponse.EES_CONTRACT_CREATED), HttpStatus.CREATED);
        }
    }

    public List<EesContract> getAllContracts() {
        List<EesContract> contracts = eesContractRepository.findAll();
        return contracts;
    }

    public ResponseEntity<Object> getContractByCode(String contractCode) {
        String prefixContractCode = contractCode;
        if (!prefixContractCode.startsWith(EesUserResponse.EES_CONTRACT_PREFIX)) {
            prefixContractCode = EesUserResponse.EES_CUSTOMER_PREFIX + contractCode;
        }
        EesContract contract = eesContractRepository.findByContractCode(prefixContractCode);
        if (contract == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CONTRACT_NOT_FOUND, contractCode)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(contract, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateContractByCode(String contractCode, EesContractRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if(user.isEmpty()){
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        String prefixContractCode = EesUserResponse.EES_CONTRACT_PREFIX + contractCode;
        EesContract contract = eesContractRepository.findByContractCode(prefixContractCode);
        if (contract == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CONTRACT_NOT_FOUND, contractCode)), HttpStatus.NOT_FOUND);
        } else {
            if (request.getContractCode().length() != 16) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CONTRACT_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {
                // First check if contract is exists by request contract code
                if (eesContractRepository.existsByContractCode(request.getContractCode()) && prefixContractCode.compareToIgnoreCase(request.getContractCode()) != 0) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CONTRACT_ALREADY_EXISTS, request.getContractCode())), HttpStatus.BAD_REQUEST);
                }
                contract.setContractCode(request.getContractCode());
                contract.setContractTitle(request.getContractTitle());
                contract.setContractDescription(request.getContractDescription());
                contract.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                contract.setCreatedBy(user.get());
                eesContractRepository.save(contract);
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CONTRACT_UPDATED), HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> changeStatusContract(String contractCode, String status) {
        EesContract contract = eesContractRepository.findByContractCode(EesUserResponse.EES_CONTRACT_PREFIX + contractCode);
        if (contract == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CONTRACT_NOT_FOUND, contractCode)), HttpStatus.NOT_FOUND);
        } else {
            contract.setStatus(status.compareToIgnoreCase(EesStatusCustomer.ACTIVE.getStatus()) == 0 ? EesStatusCustomer.DISABLED : EesStatusCustomer.ACTIVE);
            contract.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            eesContractRepository.save(contract);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CONTRACT_UPDATED_STATUS), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> deleteContractByCode(String contractCode) {
        EesContract contract = eesContractRepository.findByContractCode(EesUserResponse.EES_CONTRACT_PREFIX + contractCode);
        if (contract == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CONTRACT_NOT_FOUND, contractCode)), HttpStatus.NOT_FOUND);
        } else {
            eesContractRepository.delete(contract);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CONTRACT_DELETED), HttpStatus.OK);
        }
    }
}
