package com.enexse.intranet.ms.users.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.partials.EesCloudinaryDoc;
import com.enexse.intranet.ms.users.payload.response.EesGenericResponse;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.partials.EesCloudinaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EesCloudinaryService {

    private Cloudinary cloudinary;
    private EesCloudinaryRepository eesCloudinaryRepository;

    public ResponseEntity<Object> insertDocument(MultipartFile file, String uploadType) throws IOException {
        System.out.println("Documentation: " + file.getOriginalFilename());
        System.out.println("cloudinary: " + cloudinary);
        Map uploadResult = null;
        if (uploadType.equalsIgnoreCase(EesUserConstants.EES_CLOUDINARY_MANUAL_DOC_TYPE)) {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", EesUserConstants.EES_CLOUDINARY_MANUAL_DOC_FOLDER));
        } else {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        }

        System.out.println("uploadResult: " + uploadResult);
        EesCloudinaryDoc doc = new EesCloudinaryDoc()
                .builder()
                .eesUploadType(uploadType)
                .signature(uploadResult.get("signature") != null ? uploadResult.get("signature").toString() : null)
                .format(uploadResult.get("format") != null ? uploadResult.get("format").toString() : null)
                .resourceType(uploadResult.get("resource_type") != null ? uploadResult.get("resource_type").toString() : null)
                .secureUrl(uploadResult.get("secure_url") != null ? uploadResult.get("secure_url").toString() : null)
                .assetId(uploadResult.get("asset_id") != null ? uploadResult.get("asset_id").toString() : null)
                .versionId(uploadResult.get("version_id") != null ? uploadResult.get("version_id").toString() : null)
                .type(uploadResult.get("type") != null ? uploadResult.get("type").toString() : null)
                .version(uploadResult.get("version") != null ? uploadResult.get("version").toString() : null)
                .url(uploadResult.get("url") != null ? uploadResult.get("url").toString() : null)
                .publicId(uploadResult.get("public_id") != null ? uploadResult.get("public_id").toString() : null)
                .tags(uploadResult.get("tags") != null ? (List<String>) uploadResult.get("tags") : null)
                .pages(uploadResult.containsKey("pages") ? uploadResult.get("pages").toString() : null)
                .folder(uploadResult.get("folder") != null ? uploadResult.get("folder").toString() : null)
                .originalFilename(file.getOriginalFilename())
                .apiKey(uploadResult.get("api_key") != null ? uploadResult.get("api_key").toString() : null)
                .bytes(uploadResult.containsKey("bytes") ? uploadResult.get("bytes").toString() : null)
                .width(uploadResult.containsKey("width") ? uploadResult.get("width").toString() : null)
                .height(uploadResult.containsKey("height") ? uploadResult.get("height").toString() : null)
                .etag(uploadResult.get("etag") != null ? uploadResult.get("etag").toString() : null)
                .placeholder(uploadResult.get("placeholder") != null ? (Boolean) uploadResult.get("placeholder") : null)
                .createdAt(uploadResult.get("created_at") != null ? uploadResult.get("created_at").toString() : null)
                .build();
        eesCloudinaryRepository.save(doc);
        return new ResponseEntity<Object>(new EesGenericResponse(uploadResult, EesUserResponse.EES_CLOUDINARY_UPLOADED), HttpStatus.CREATED);
    }

    public List<EesCloudinaryDoc> getDocuments() {
        List<EesCloudinaryDoc> docs = eesCloudinaryRepository.findAll();
        return docs;
    }

    public ResponseEntity<Object> deleteDocument(String publicId) throws IOException {

        EesCloudinaryDoc doc = eesCloudinaryRepository.findByPublicId(publicId);
        if (doc != null) {
            // Delete in Cloudinary Stockage
            Map deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Delete in Mongo Db
            eesCloudinaryRepository.delete(doc);
            return new ResponseEntity<Object>(new EesGenericResponse(doc, EesUserResponse.EES_CLOUDINARY_DELETED), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CLOUDINARY_DOC_NOT_FOUND, publicId)), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> findDocument(String eesUploadType) {
        EesCloudinaryDoc doc = eesCloudinaryRepository.findByEesUploadType(eesUploadType);
        if (doc != null) {
            return new ResponseEntity<>(new EesGenericResponse(doc, EesUserResponse.EES_CLOUDINARY_FOUND), HttpStatus.OK);
        }
        return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CLOUDINARY_DOC_NOT_FOUND, eesUploadType)), HttpStatus.OK);
    }
}
