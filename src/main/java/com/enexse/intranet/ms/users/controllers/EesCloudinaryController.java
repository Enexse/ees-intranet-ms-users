package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.partials.EesCloudinaryDoc;
import com.enexse.intranet.ms.users.services.EesCloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesCloudinaryController {

    private EesCloudinaryService eesCloudinaryService;

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @PostMapping(EesUserEndpoints.EES_CLOUDINARY_INSERT_FILE)
    public ResponseEntity<Object> eesInsertDocument(@RequestParam(value = "doc", required = true) MultipartFile doc, @RequestParam(value = "uploadType", required = true) String uploadType) throws IOException {
        return eesCloudinaryService.insertDocument(doc, uploadType);
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @GetMapping(EesUserEndpoints.EES_CLOUDINARY_GET_ALL_FILES)
    public List<EesCloudinaryDoc> eesGetDocuments() {
        return eesCloudinaryService.getDocuments();
    }

    @RolesAllowed(EesUserConstants.EES_ROLE_ADMINISTRATOR)
    @DeleteMapping(EesUserEndpoints.EES_CLOUDINARY_DELETE_FILE)
    public ResponseEntity<Object> eesDeleteDocument(@RequestParam(value = "publicId", required = true) String publicId) throws IOException {
        return eesCloudinaryService.deleteDocument(publicId);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_CLOUDINARY_FIND_FILE)
    public ResponseEntity<Object> eesFindDocument(@RequestParam(value = "eesUploadType", required = true) String eesUploadType) {
        return eesCloudinaryService.findDocument(eesUploadType);
    }
}
