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

    @RolesAllowed({EesUserConstants.EES_ROLE_ADMINISTRATOR, EesUserConstants.EES_ROLE_RESPONSABLE})
    @PostMapping(EesUserEndpoints.EES_CLOUDINARY_INSERT_FILE)
    public ResponseEntity<Object> eesInsertDocument(
            @RequestParam(value = "doc", required = true) MultipartFile doc,
            @RequestParam(value = "uploadType", required = true) String uploadType) throws IOException {
        return eesCloudinaryService.insertDocument(doc, uploadType);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @PutMapping(EesUserEndpoints.EES_CLOUDINARY_ATTACHMENT_FILES)
    public ResponseEntity<Object> eesAttachmentDocuments(
            @PathVariable String userId,
            @RequestParam(value = "uploadType", required = true) String uploadType,
            @RequestParam(value = "folder", required = true) String folder,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> docs) throws IOException {
        return eesCloudinaryService.attachmentDocuments(userId, uploadType, folder, docs);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_CLOUDINARY_GET_ALL_FILES)
    public List<EesCloudinaryDoc> eesGetDocuments() {
        return eesCloudinaryService.getDocuments();
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_CLOUDINARY_GET_ALL_FILES_USER)
    public List<EesCloudinaryDoc> eesGetDocumentsByUserId(@PathVariable String userId) {
        return eesCloudinaryService.getDocumentsByUserId(userId);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_CLOUDINARY_GET_ALL_FILES_USER_UPLOAD_TYPE)
    public List<EesCloudinaryDoc> eesGetDocumentsByUserIdAndUploadType(@PathVariable String userId,
                                                                       @RequestParam(value = "eesUploadType", required = true) String eesUploadType) {
        return eesCloudinaryService.getDocumentsByUserIdAndUploadType(userId, eesUploadType);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_CLOUDINARY_FIND_FILE_BY_ID_USERID)
    public EesCloudinaryDoc eesGetDocumentByIdAndUserId(@PathVariable String id, @PathVariable String userId) {
        return eesCloudinaryService.getDocumentByIdAndUserId(id, userId);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @DeleteMapping(EesUserEndpoints.EES_CLOUDINARY_DELETE_FILE_BY_PUBLIC_ID)
    public ResponseEntity<Object> eesDeleteDocumentByPublicId(@RequestParam(value = "publicId", required = true) String publicId) throws IOException {
        return eesCloudinaryService.deleteDocumentByPublicId(publicId);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @DeleteMapping(EesUserEndpoints.EES_CLOUDINARY_DELETE_FILE_BY_ID)
    public ResponseEntity<Object> eesDeleteDocumentById(@RequestParam(value = "id", required = true) String id) throws IOException {
        return eesCloudinaryService.deleteDocumentById(id);
    }

    @RolesAllowed(EesUserConstants.EES_DEFAULT_ROLES)
    @GetMapping(EesUserEndpoints.EES_CLOUDINARY_FIND_FILE)
    public ResponseEntity<Object> eesFindDocument(@RequestParam(value = "eesUploadType", required = true) String eesUploadType) {
        return eesCloudinaryService.findDocument(eesUploadType);
    }
}
