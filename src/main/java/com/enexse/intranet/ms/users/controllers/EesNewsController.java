package com.enexse.intranet.ms.users.controllers;


import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesNews;
import com.enexse.intranet.ms.users.payload.request.EesNewsRequest;
import com.enexse.intranet.ms.users.services.EesNewsService;
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
public class EesNewsController {
    private EesNewsService eesNewsService;

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PostMapping(EesUserEndpoints.EES_POST_NEWS)
    public ResponseEntity<Object> eesInsert(@Valid @RequestBody EesNewsRequest request) {
        return eesNewsService.addNew(request);
    }

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR,EesUserConstants.EES_ROLE_COLLABORATOR,EesUserConstants.EES_ROLE_RESPONSABLE})
    @GetMapping(EesUserEndpoints.EES_GET_All_NEWS)
    public List<EesNews> getNews() {
        return eesNewsService.getAllNews();
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_GET_NEWS)
    public ResponseEntity<Object> getAnew(@PathVariable String id) {
        return eesNewsService.getNewById(id);
    }
    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_CHANGE_STATUS)
    public ResponseEntity<Object> eesChangeStatus(@PathVariable String id,
                                                  @RequestParam boolean Status) {
        return eesNewsService.changeStatus(id, Status);
    }
    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PutMapping(EesUserEndpoints.EES_UPDATE_NEWS)
    public ResponseEntity<Object> eesUpdateCustomerByCode(@PathVariable String id,
                                                          @RequestBody EesNewsRequest request) {
        return eesNewsService.updateANew(id, request);
    }
    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_ID)
    public ResponseEntity<Object> eesDeleteTheNew(@PathVariable String id) {
        return eesNewsService.deleteTheNew(id);
    }
}
