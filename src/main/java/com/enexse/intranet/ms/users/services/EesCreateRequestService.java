package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.enums.EesStatusRequest;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserCreateRequest;
import com.enexse.intranet.ms.users.models.EesUserRequest;
import com.enexse.intranet.ms.users.models.EesUserSubRequest;
import com.enexse.intranet.ms.users.payload.request.EesCreateRequest;
import com.enexse.intranet.ms.users.payload.request.EesResponseManualRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesCreateRequestRepository;
import com.enexse.intranet.ms.users.repositories.EesRequestRepository;
import com.enexse.intranet.ms.users.repositories.EesSubRequestRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@AllArgsConstructor
public class EesCreateRequestService {

    private EesRequestRepository eesRequestRepository;
    private EesSubRequestRepository eesSubRequestRepository;

    private EesCreateRequestRepository eesCreateRequestRepository;
    private EesUserRepository eesUserRepository;

    private EesMailService eesMailService;

    private GridFsTemplate gridFsTemplate;


    private static final Logger logger = LoggerFactory.getLogger(EesCreateRequestService.class);

    public ResponseEntity<Object> createRequest(EesCreateRequest request,List<MultipartFile> files){
        EesUserRequest userRequest = eesRequestRepository.findByCode(request.getRequestCode());
        Optional<EesUserSubRequest> userSubRequest = eesSubRequestRepository.findByCode(request.getSubRequestCode());
        Optional<EesUser> recipient = eesUserRepository.findByUserId(request.getRecipientId());
        Optional<EesUser> user = eesUserRepository.findByUserId(request.getUserId()) ;
        Optional<EesUser> referent = eesUserRepository.findByUserId(request.getReferentId());

        if (files != null) {
            long totalSize = 0;
            for (MultipartFile file : files) {
                totalSize += file.getSize();
                if (totalSize > EesUserConstants.EES_AVATAR_MAX_SIZE) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_ATTACH_FILES)), HttpStatus.BAD_REQUEST);
                }
                // Check file format
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                List<String> allowedExtensions = EesUserConstants.EES_ALLOWED_EXTENTIONS_REQUEST;
                if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
                    StringBuilder extentions = new StringBuilder();
                    for (String ex : allowedExtensions) {
                        extentions.append(ex).append(" ");
                    }
                    return new ResponseEntity<>("Invalid file format. Allowed formats: " + extentions, HttpStatus.BAD_REQUEST);
                }
            }
        }

        EesUserCreateRequest createRequest = new EesUserCreateRequest()
                .builder()
                .request(userRequest)
                .subRequest(userSubRequest.get())
                .recipient(recipient.get())
                .referent(referent.get())
                .user(user.get())
                .personalEmail(user.get().getPersonalEmail())
                .type("MANUAL")
                .comments(request.getComments())
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .status(EesStatusRequest.PENDING)
                .build();

        // Check if a file was uploaded
        if (files != null && !files.isEmpty()) {
            HashMap<String, String> fileIdsMap = new HashMap<>();
            for (MultipartFile file : files) {
                try {
                    String fileId = uploadFile(file);
                    fileIdsMap.put(fileId, file.getOriginalFilename());
                } catch (Exception e) {
                    // Handle file upload error
                    return new ResponseEntity<>(new EesMessageResponse("Error uploading the file."), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            createRequest.setAttachmentFileIds(fileIdsMap);
        }

        eesCreateRequestRepository.save(createRequest);
        return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_CREATE_REQUEST_CREATED)), HttpStatus.OK);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            // Store the file in GridFS and get the file ID
            ObjectId fileId = gridFsTemplate.store(inputStream, file.getOriginalFilename());
            return fileId.toString();
        } catch (IOException e) {
            throw new IOException("Error uploading file to MongoDB GridFS: " + e.getMessage(), e);
        }
    }


    public ResponseEntity<Object> downloadFile(String fileId){
        // Retrieve the file from GridFS
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(fileId))));

        if (gridFSFile != null) {
            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    public ResponseEntity<Object> updateAutomaticRequestStatus(String requestId) {
        Optional<EesUserCreateRequest> requestOptional = eesCreateRequestRepository.findById(requestId);
        EesUserCreateRequest automaticRequest  = requestOptional.get() ;
        if(requestOptional.isPresent()) {
            automaticRequest.setStatus(EesStatusRequest.SENT);
            automaticRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            eesCreateRequestRepository.save(automaticRequest);
            System.out.println(automaticRequest.getStatus());
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_STATUS_UPDATED)), HttpStatus.OK);
        } else  {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateRequestStatus(String requestId, EesResponseManualRequest request) {
        Optional<EesUserCreateRequest> requestOptional = eesCreateRequestRepository.findById(requestId);

        if (requestOptional.isPresent()) {
            EesUserCreateRequest requestManual = requestOptional.get();
            requestManual.setStatus(request.getStatus());
            requestManual.setComments(request.getNote());
            requestManual.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            String enexseEmail =requestManual.getUser().getEnexseEmail();
            eesCreateRequestRepository.save(requestManual );

            try {
                if (  requestManual.getType().equalsIgnoreCase("MANUAL")) {
                    eesMailService.UpdatedStatusEmail( requestManual.getPersonalEmail()
                            ,   requestManual.getRequest().getRequestTitle(), enexseEmail, requestManual.getCreatedAt(),   requestManual.getRequest().getRequestTitle(),  request, EesUserConstants.EES_VERIFY_TYPE_UPDATE_REQUEST_STATUS);
                    logger.info("Email sent successfully");
                } else if (  requestManual.getType().equalsIgnoreCase("AUTOMATIC")) {
                    eesMailService.UpdatedStatusEmail(  requestManual.getPersonalEmail(), requestManual.getRequest().getRequestTitle(),enexseEmail, requestManual.getCreatedAt(), requestManual.getRequest().getRequestTitle(), null, EesUserConstants.EES_VERIFY_TYPE_INVITATION_USER);
                }

                //logger.info("Email sent successfully");
                System.out.println(  requestManual.getRequest().getRequestTitle());

                return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_STATUS_UPDATED)), HttpStatus.OK);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(ex);

                return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_ERROR_SEND_EMAIL +  requestManual.getPersonalEmail())), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getListOfRequests(){
        List<EesUserCreateRequest> list = null;
        list = eesCreateRequestRepository.findAll();
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<Object> getListOfRecipients() {
        List<EesUserCreateRequest> list = eesCreateRequestRepository.findAll();
        List<EesUser> recipients = new ArrayList<>();
        Set<String> recipientIds = new HashSet<>(); // Set to store recipient IDs

        for (EesUserCreateRequest request : list) {
            if (request.getRecipient() != null && !recipientIds.contains(request.getRecipient().getUserId())) {
                recipients.add(request.getRecipient());
                recipientIds.add(request.getRecipient().getUserId());
            }
        }

        return ResponseEntity.ok(recipients);
    }



    public ResponseEntity<Object> getListOfReferents(){
        List<EesUserCreateRequest> list = eesCreateRequestRepository.findAll();;
        List<EesUser> referents = new ArrayList<>();
        Set<String> referentIds = new HashSet<>(); // Set to store referent IDs
        for(EesUserCreateRequest request : list){
            if(request.getReferent() != null && !referentIds.contains(request.getReferent().getUserId())){
                referents.add(request.getReferent());
                referentIds.add(request.getReferent().getUserId());
            }
        }
        return ResponseEntity.ok(referents);
    }

    public ResponseEntity<Object> getRequestById(String id){
        Optional<EesUserCreateRequest> request = eesCreateRequestRepository.findById(id);
        System.out.println(request.get().getStatus());
        if(request.isEmpty()){
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(request);
    }

    public List<EesUserCreateRequest> getAllManualRequestsByUser(String userId) {
        EesUser user = eesUserRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with userId: " + userId));
        return eesCreateRequestRepository.findAllByUserAndType(user, "MANUAL");
    }

    public List<EesUserCreateRequest> getLastRequests(){

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 5, sort);

        return  eesCreateRequestRepository.findLastRequestsAfterDate(EesCommonUtil.generateCurrentDateUtil(),pageable);

    }



}
