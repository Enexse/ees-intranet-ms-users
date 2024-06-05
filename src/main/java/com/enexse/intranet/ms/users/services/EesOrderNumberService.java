package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesCustomer;
import com.enexse.intranet.ms.users.models.EesNews;
import com.enexse.intranet.ms.users.models.EesOrderNumber;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.payload.request.EesOrderNumberRequest;
import com.enexse.intranet.ms.users.payload.response.EesGenericResponse;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesCustomerRepository;
import com.enexse.intranet.ms.users.repositories.EesOrderNumberRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EesOrderNumberService {

    private EesUserRepository eesUserRepository;
    private EesCustomerRepository eesCustomerRepository;
    private EesOrderNumberRepository eesOrderNumberRepository;

    public ResponseEntity<Object> insertOrderNumber(EesOrderNumberRequest request) {
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

        EesOrderNumber existingNumber = eesOrderNumberRepository.findByCode(request.getCode().toUpperCase(Locale.ROOT));

        if (existingNumber != null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ORDER_NUMBER_ALREADY_EXISTS, request.getCode())), HttpStatus.BAD_REQUEST);
        } else {
            EesOrderNumber orderNumber = new EesOrderNumber()
                    .builder()
                    .code(request.getCode().toUpperCase(Locale.ROOT))
                    .customer(customer.get())
                    .from(request.getValidity().getFrom())
                    .to(request.getValidity().getTo())
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .createdBy(user.get())
                    .build();
            eesOrderNumberRepository.save(orderNumber);
            return new ResponseEntity<Object>(new EesGenericResponse(orderNumber, EesUserResponse.EES_ORDER_NUMBER_CREATED), HttpStatus.CREATED);
        }
    }

    public List<EesOrderNumber> getAllOrderNumbers() {
        List<EesOrderNumber> orderNumbers = eesOrderNumberRepository.findAll()
                .stream().sorted(Comparator.comparing(EesOrderNumber::getCreatedAt).reversed()).collect(Collectors.toList());
        return orderNumbers;
    }

    public ResponseEntity<Object> getOrderNumberCode(String code) {
        EesOrderNumber orderNumber = eesOrderNumberRepository.findByCode(code);
        if (orderNumber == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ORDER_NUMBER_NOT_FOUND, code)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(orderNumber, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateOrderNumberByCode(String code, EesOrderNumberRequest request) {
        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        Optional<EesCustomer> customer = null;
        customer = eesCustomerRepository.findById(request.getCustomerId());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        EesOrderNumber orderNumber = eesOrderNumberRepository.findByCode(code);
        if (orderNumber == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ORDER_NUMBER_NOT_FOUND, code)), HttpStatus.NOT_FOUND);
        } else {
            if (!(orderNumber.getCode().equals(request.getCode()))) {
                EesOrderNumber existingNumber = eesOrderNumberRepository.findByCode(request.getCode());
                if (existingNumber != null) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CASE_NUMBER_ALREADY_EXISTS, request.getCode())), HttpStatus.BAD_REQUEST);
                }
            }
            orderNumber.setCode(request.getCode());
            orderNumber.setCustomer(customer.get());
            orderNumber.setCreatedBy(user.get());
            orderNumber.setFrom(request.getValidity().getFrom());
            orderNumber.setTo(request.getValidity().getTo());
            orderNumber.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            eesOrderNumberRepository.save(orderNumber);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ORDER_NUMBER_UPDATED), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> deleteOrderNumberByCode(String code) {
        EesOrderNumber orderNumber = eesOrderNumberRepository.findByCode(code);

        if (orderNumber != null) {
            eesOrderNumberRepository.delete(orderNumber);
            return new ResponseEntity<>(new EesMessageResponse(EesUserResponse.EES_ORDER_NUMBER_DELETED), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
