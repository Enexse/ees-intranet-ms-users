package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.services.EesUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesTestDeployProdController {

    @Autowired
    private EesUserService eesUserService;

    @GetMapping("/test")
    public String getMessage() {
        return "Hello World Users Service";
    }

    @GetMapping("/users")
    public ResponseEntity<Object> eesGetAllCollaborators() {
        return eesUserService.getAllCollaborators();
    }
}
