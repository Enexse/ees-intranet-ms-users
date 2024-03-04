package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesOrderNumber;
import com.enexse.intranet.ms.users.payload.request.EesOrderNumberRequest;
import com.enexse.intranet.ms.users.services.EesOrderNumberService;
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
public class EesOrderNumberController {

    private EesOrderNumberService eesOrderNumberService;

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_INSERT_ORDER_NUMBER)
    public ResponseEntity<Object> eesInsertOrderNumber(@Valid @RequestBody EesOrderNumberRequest request) {
        return eesOrderNumberService.insertOrderNumber(request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_All_ORDER_NUMBERS)
    public List<EesOrderNumber> eesGetAllOrderNumbers() {
        return eesOrderNumberService.getAllOrderNumbers();
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_GET_ORDER_NUMBER_BY_CODE)
    public ResponseEntity<Object> eesGetOrderNumber(@PathVariable String code) {
        return eesOrderNumberService.getOrderNumberCode(code);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_ORDER_NUMBER_BY_CODE)
    public ResponseEntity<Object> eesUpdateOrderNumberByCode(@PathVariable String code,
                                                             @RequestBody EesOrderNumberRequest request) {
        return eesOrderNumberService.updateOrderNumberByCode(code, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_ORDER_NUMBER_BY_CODE)
    public ResponseEntity<Object> eesDeleteOrderNumberByCode(@PathVariable String code) {
        return eesOrderNumberService.deleteOrderNumberByCode(code);
    }
}
