package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesCustomer;
import com.enexse.intranet.ms.users.payload.request.EesCustomerRequest;
import com.enexse.intranet.ms.users.services.EesCustomerService;
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
public class EesCustomerController {

    private EesCustomerService eesCustomerService;

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PostMapping(EesUserEndpoints.EES_INSERT_CUSTOMER)
    public ResponseEntity<Object> eesInsertCustomer(@Valid @RequestBody EesCustomerRequest request) {
        return eesCustomerService.insertCustomer(request);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_All_CUSTOMERS)
    public List<EesCustomer> eesGetAllCustomers() {
        return eesCustomerService.getAllCustomers();
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_CUSTOMER_BY_CODE)
    public ResponseEntity<Object> eesGetCustomerByCode(@PathVariable String customerCode) {
        return eesCustomerService.getCustomerByCode(customerCode);
    }

//    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_CUSTOMER_BY_ID)
    public EesCustomer eesGetCustomerById(@PathVariable String customerId) {
        return eesCustomerService.getCustomerById(customerId);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_UPDATE_CUSTOMER_BY_CODE)
    public ResponseEntity<Object> eesUpdateCustomerByCode(@PathVariable String customerCode,
                                                          @RequestBody EesCustomerRequest request,
                                                          @RequestParam(value = "referentId", required = true) String referentId) {
        return eesCustomerService.updateCustomerByCode(customerCode, request, referentId);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_ACTIVATE_DISABLED_CUSTOMER_BY_CODE)
    public ResponseEntity<Object> eesChangeStatusCustomer(@PathVariable String customerCode,
                                                          @RequestParam(value = "status", required = true) String status) {
        return eesCustomerService.changeStatusCustomer(customerCode, status);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @DeleteMapping(EesUserEndpoints.EES_DELETE_CUSTOMER_BY_CODE)
    public ResponseEntity<Object> eesDeleteCustomerByCode(@PathVariable String customerCode) {
        return eesCustomerService.deleteCustomerByCode(customerCode);
    }
}
