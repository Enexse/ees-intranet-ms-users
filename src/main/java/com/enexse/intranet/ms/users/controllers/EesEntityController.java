package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserEntity;
import com.enexse.intranet.ms.users.payload.request.EesEntityRequest;
import com.enexse.intranet.ms.users.services.EesUserEntityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesEntityController {

    private EesUserEntityService eesUserEntityService;

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_INSERT_ENTITY)
    public ResponseEntity<Object> eesInsertEntity(@Valid @RequestBody EesEntityRequest request) {
        return eesUserEntityService.insertEntity(request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_All_ENTITIES)
    public List<EesUserEntity> eesGetAllEntities() {
        return eesUserEntityService.getAllEntities();
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_ENTITY_BY_CODE_ENTITY)
    public EesUserEntity eesGetEntityByCodeEntity(@PathVariable String entityCode) {
        return eesUserEntityService.getEntityByCode(entityCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_ENTITY)
    public HttpEntity<Object> eesUpdateEntityByCodeEntity(@PathVariable String entityCode, @RequestBody EesEntityRequest request) {
        return eesUserEntityService.updateEntityByCode(entityCode, request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_USERS_BY_ENTITY)
    public Optional<List<EesUser>> eesGetUsersByEntity(@PathVariable String entityCode) {
        return eesUserEntityService.getUsersEntity(entityCode);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_ENTITY)
    public ResponseEntity<Object> eesDeleteEntityByCode(@PathVariable String entityCode) {
        return eesUserEntityService.deleteEntityByCode(entityCode);
    }
}
