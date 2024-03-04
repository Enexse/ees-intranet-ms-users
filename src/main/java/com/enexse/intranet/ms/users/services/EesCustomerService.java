package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.enums.EesStatusCustomer;
import com.enexse.intranet.ms.users.models.EesCustomer;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.partials.EesReferentCustomer;
import com.enexse.intranet.ms.users.payload.request.EesCustomerRequest;
import com.enexse.intranet.ms.users.payload.response.EesGenericResponse;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesCustomerRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.repositories.partials.EesReferentCustomerRepository;
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
public class EesCustomerService {

    private EesCustomerRepository eesCustomerRepository;
    private EesReferentCustomerRepository eesReferentCustomerRepository;
    private EesUserRepository eesUserRepository;


    public ResponseEntity<Object> insertCustomer(EesCustomerRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }
        if (!request.getCustomerCode().startsWith(EesUserResponse.EES_CUSTOMER_PREFIX) || request.getCustomerCode().length() != 14) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CUSTOMER_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }

        EesCustomer existingCustomer = eesCustomerRepository.findByCustomerCode(request.getCustomerCode().toUpperCase(Locale.ROOT));

        if (existingCustomer != null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CUSTOMER_ALREADY_EXISTS, request.getCustomerCode())), HttpStatus.BAD_REQUEST);
        } else {
            EesReferentCustomer referent = new EesReferentCustomer()
                    .builder()
                    .firstName(request.getReferent().getFirstName())
                    .lastName(request.getReferent().getLastName())
                    .profession(request.getReferent().getProfession())
                    .email(request.getReferent().getEmail())
                    .phoneNumbers(request.getReferent().getPhoneNumbers())
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .build();
            EesCustomer customer = new EesCustomer()
                    .builder()
                    .customerCode(request.getCustomerCode().toUpperCase(Locale.ROOT))
                    .customerTitle(request.getCustomerTitle())
                    .sectorField(request.getSectorField())
                    .status(EesStatusCustomer.ACTIVE)
                    .comments(request.getComments())
                    .referent(referent)
                    .landline(request.getLandline())
                    .fax(request.getFax())
                    .website(request.getWebsite())
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .createdBy(user.get())
                    .build();
            eesReferentCustomerRepository.save(referent);
            eesCustomerRepository.save(customer);
            return new ResponseEntity<Object>(new EesGenericResponse(customer, EesUserResponse.EES_CUSTOMER_CREATED), HttpStatus.CREATED);
        }
    }

    public List<EesCustomer> getAllCustomers() {
        List<EesCustomer> customers = eesCustomerRepository.findAll();
        return customers;
    }

    public ResponseEntity<Object> getCustomerByCode(String customerCode) {
        String prefixCustomerCode = customerCode;
        if (!prefixCustomerCode.startsWith(EesUserResponse.EES_CUSTOMER_PREFIX)) {
            prefixCustomerCode = EesUserResponse.EES_CUSTOMER_PREFIX + customerCode;
        }
        EesCustomer customer = eesCustomerRepository.findByCustomerCode(prefixCustomerCode);
        if (customer == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CUSTOMER_NOT_FOUND, customerCode)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(customer, HttpStatus.OK);
    }

    public EesCustomer getCustomerById(String customerId) {
        EesCustomer customer = eesCustomerRepository.findByCustomerId(customerId);
        return customer;
    }

    public ResponseEntity<Object> updateCustomerByCode(String customerCode, EesCustomerRequest request, String referentId) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }
        String prefixCustomerCode = EesUserResponse.EES_CUSTOMER_PREFIX + customerCode;
        EesCustomer customer = eesCustomerRepository.findByCustomerCode(prefixCustomerCode);
        EesReferentCustomer referentCustomer = eesReferentCustomerRepository.findByReferentId(referentId);
        if (customer == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CUSTOMER_NOT_FOUND, customerCode)), HttpStatus.NOT_FOUND);
        } else {
            if (request.getCustomerCode().length() != 14) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CUSTOMER_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {
                // First check if customer exists by request customer code
                if (eesCustomerRepository.existsByCustomerCode(request.getCustomerCode()) && prefixCustomerCode.compareToIgnoreCase(request.getCustomerCode()) != 0) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CUSTOMER_ALREADY_EXISTS, request.getCustomerCode())), HttpStatus.BAD_REQUEST);
                }
                referentCustomer.setFirstName(request.getReferent().getFirstName());
                referentCustomer.setLastName(request.getReferent().getLastName());
                referentCustomer.setProfession(request.getReferent().getProfession());
                referentCustomer.setEmail(request.getReferent().getEmail());
                referentCustomer.setPhoneNumbers(request.getReferent().getPhoneNumbers());
                eesReferentCustomerRepository.save(referentCustomer);
                customer.setCustomerCode(request.getCustomerCode());
                customer.setCustomerTitle(request.getCustomerTitle());
                customer.setWebsite(request.getWebsite());
                customer.setSectorField(request.getSectorField());
                customer.setFax(request.getFax());
                customer.setLandline(request.getLandline());
                customer.setComments(request.getComments());
                customer.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                customer.setCreatedBy(user.get());
                eesCustomerRepository.save(customer);
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CUSTOMER_UPDATED), HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> deleteCustomerByCode(String customerCode) {
        EesCustomer customer = eesCustomerRepository.findByCustomerCode(EesUserResponse.EES_CUSTOMER_PREFIX + customerCode);
        if (customer == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CUSTOMER_NOT_FOUND, customerCode)), HttpStatus.NOT_FOUND);
        } else {
            eesCustomerRepository.delete(customer);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CUSTOMER_DELETED), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> changeStatusCustomer(String customerCode, String status) {
        EesCustomer customer = eesCustomerRepository.findByCustomerCode(EesUserResponse.EES_CUSTOMER_PREFIX + customerCode);
        if (customer == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CUSTOMER_NOT_FOUND, customerCode)), HttpStatus.NOT_FOUND);
        } else {
            customer.setStatus(status.compareToIgnoreCase(EesStatusCustomer.ACTIVE.getStatus()) == 0 ? EesStatusCustomer.DISABLED : EesStatusCustomer.ACTIVE);
            customer.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            eesCustomerRepository.save(customer);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_CUSTOMER_UPDATED_STATUS), HttpStatus.OK);
        }
    }
}
