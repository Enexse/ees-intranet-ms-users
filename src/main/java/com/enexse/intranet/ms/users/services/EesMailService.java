package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesGroup;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.partials.EesCloudinaryDoc;
import com.enexse.intranet.ms.users.payload.request.EesCollaboratorEmailRequest;
import com.enexse.intranet.ms.users.payload.request.EesGroupEmailRequest;
import com.enexse.intranet.ms.users.payload.request.EesResponseManualRequest;
import com.enexse.intranet.ms.users.payload.response.EesGenericResponse;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesGroupRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.repositories.partials.EesCloudinaryRepository;
import com.enexse.intranet.ms.users.utils.EesMailUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class EesMailService {

    private EesUserRepository eesUserRepository;

    private EesGroupRepository eesGroupRepository;
    private EesMailUtil eesMailUtil;

    private EesCloudinaryRepository cloudinaryRepository;

    public ResponseEntity<Object> invitationUserEmail(String email, String password, String verifyType) {
        Optional<EesUser> user = null;
        Map<String, Object> model = new HashMap<>();
        CompletableFuture<EesGenericResponse> response = null;

        try {
            user = eesUserRepository.findByPersonalEmail(email);

            if (user.isPresent()) {
                model.put("name", user.get().getLastName().toUpperCase());
                model.put("email", user.get().getPersonalEmail());
                model.put("userId", user.get().getUserId().toUpperCase());
                model.put("enexseEmail", user.get().getEnexseEmail());
                model.put("firstPassword", user.get().getFirstName() + "@1234");

                // recuperate the file from cloudinary
                EesCloudinaryDoc booklet = cloudinaryRepository.findByEesUploadType(EesUserConstants.EES_CLOUDINARY_WELCOMEL_BOOKLET_TYPE);
                String bookletUrl = booklet.getSecureUrl();
                byte[] bookletData = new URL(bookletUrl).openStream().readAllBytes();
                ByteArrayResource bookletResource = new ByteArrayResource(bookletData) {
                    @Override
                    public String getFilename() {
                        return booklet.getOriginalFilename(); // Set the filename of the Cloudinary file
                    }
                };

                //convert byteArray to multipart file
                MultipartFile bookletFile = convertByteArrayResourceToMultipartFile(bookletResource, booklet.getFormat());

                // Add the MultipartFile to the attachments list
                List<MultipartFile> attachments = new ArrayList<>();
                attachments.add(bookletFile);

                // send the email with the attachments
                response = eesMailUtil.eesSendMailWithAttachement(user.get().getPersonalEmail(), model, verifyType, attachments);
                return new ResponseEntity<Object>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new EesMessageResponse(
                        EesUserResponse.EES_USER_NOT_FOUND + email),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new ResponseEntity<Object>(new EesMessageResponse(
                    EesUserResponse.EES_USER_ERROR_SEND_EMAIL + email),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public MultipartFile convertByteArrayResourceToMultipartFile(ByteArrayResource resource, String contentType) throws IOException {
        DiskFileItem fileItem = new DiskFileItem(
                "file", // Field name
                contentType, // Content type
                false,
                resource.getFilename(), // File name
                (int) resource.contentLength(), // File size
                null // File repository
        );

        try (InputStream inputStream = resource.getInputStream()) {
            // Copy the bytes from the ByteArrayResource to the DiskFileItem
            org.apache.commons.io.IOUtils.copy(inputStream, fileItem.getOutputStream());
        }

        // Create a MultipartFile from the DiskFileItem
        return new CommonsMultipartFile(fileItem);
    }


    public ResponseEntity<Object> sendEmailToGroups(EesGroupEmailRequest request, List<MultipartFile> attachments) {
        // Retrieve the authenticated user's information from the token
        KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
        String authenticatedUserName = principal.getKeycloakSecurityContext().getToken().getName();

        try {
            Optional<EesGroup> group = eesGroupRepository.findByCode(request.getGroupCode());

            if (group.isPresent()) {
                EesGroup eesGroup = group.get();
                List<EesUser> collaborators = eesGroup.getCollaborators();

                List<String> toEmails = new ArrayList<>();
                List<String> ccEmails = new ArrayList<>();

                for (EesUser collaborator : collaborators) {
                    String email = collaborator.getPersonalEmail();
                    toEmails.add(email);

                    for (EesUser otherCollaborator : collaborators) {
                        String otherEmail = otherCollaborator.getPersonalEmail();
                        if (!email.equals(otherEmail)) {
                            ccEmails.add(otherEmail);
                        }
                    }
                }

                Map<String, Object> model = new HashMap<>();
                model.put("sentBy", authenticatedUserName);
                model.put("subject", request.getSubject());
                model.put("personalizedMessage", request.getMessage());
                model.put("emails", toEmails);
                model.put("ccEmails", ccEmails);
                model.put("groupName", eesGroup.getGroupTitle());
                model.put("users", collaborators);

                // Remove the CC recipients
                ccEmails.clear();

                CompletableFuture<EesGenericResponse> response = eesMailUtil.eesSendGroupMail(toEmails, ccEmails, model, request.getVerifyType(), request.getSubject(), request.getMessage(), attachments);

                // Wait for the email to be sent
                response.join();

                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_SEND_MAIL_GROUP), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_GROUP_NOT_FOUND + request.getGroupCode()), HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<Object>("Failed to send group email: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> sendEmailToCollaborator(EesCollaboratorEmailRequest request, List<MultipartFile> attachments) {
        // Retrieve the authenticated user's information from the token
        KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal<KeycloakSecurityContext> principal = (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
        String authenticatedUserName = principal.getKeycloakSecurityContext().getToken().getName();

        try {

            Map<String, Object> model = new HashMap<>();
            model.put("sentBy", authenticatedUserName);
            model.put("subject", request.getSubject());
            model.put("personalizedMessage", request.getMessage());
            model.put("collaboratorName", returnUser(request.getEmail()).get().getFirstName() + " " + returnUser(request.getEmail()).get().getLastName());

            CompletableFuture<EesGenericResponse> response = eesMailUtil.eesSendCollaboratorMail(request.getEmail(), model, request.getVerifyType(), request.getSubject(), request.getMessage(), attachments);

            // Wait for the email to be sent
            response.join();

            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_SEND_MAIL_GROUP), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<Object>("Failed to send group email: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public Optional<EesUser> returnUser(String email) {
        Optional<EesUser> eesUser = eesUserRepository.findByPersonalEmail(email);
        return eesUser;
    }

    public ResponseEntity<Object> certificationUserEmail(String email, String link, String verifyType) {
        Optional<EesUser> user = null;
        Map<String, Object> model = new HashMap<>();
        CompletableFuture<EesGenericResponse> response = null;

        try {
            user = eesUserRepository.findByEnexseEmail(email);

            if (user.isPresent()) {
                model.put("name", user.get().getLastName().toUpperCase());
                model.put("email", user.get().getEnexseEmail());
                model.put("userId", user.get().getUserId().toUpperCase());
                model.put("link", link);
                response = eesMailUtil.eesSendMail(user.get().getEnexseEmail(), model, verifyType);
            }
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<Object>(new EesMessageResponse(
                    EesUserResponse.EES_USER_ERROR_SEND_EMAIL + email),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> sendForgotPasswordLinkToUserEmail(Optional<EesUser> user, String email, String link, String verifyType) {
        Map<String, Object> model = new HashMap<>();
        CompletableFuture<EesGenericResponse> response = null;

        try {
            if (user.isPresent()) {
                model.put("name", user.get().getLastName().toUpperCase());
                model.put("email", user.get().getPersonalEmail());
                model.put("userId", user.get().getUserId().toUpperCase());
                model.put("link", link);
                response = eesMailUtil.eesSendMail(user.get().getPersonalEmail(), model, verifyType);
            }
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<Object>(new EesMessageResponse(
                    EesUserResponse.EES_USER_ERROR_SEND_EMAIL + email),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> UpdatedStatusEmail(String email, String Title, String enexseEmail, String creationDate, String subRequest, EesResponseManualRequest request, String verifyType) {
        Optional<EesUser> user = null;
        Map<String, Object> model = new HashMap<>();
        CompletableFuture<EesGenericResponse> response = null;

        try {
            user = eesUserRepository.findByPersonalEmail(email);

            if (user.isPresent()) {
                model.put("name", user.get().getLastName().toUpperCase());
                model.put("email", user.get().getPersonalEmail());
                model.put("userId", user.get().getUserId().toUpperCase());
                model.put("Title", Title);
                model.put("creationDate", creationDate);
                model.put("subRequest", subRequest);
                model.put("NewStatus", request.getStatus());
                model.put("note", request.getNote());
                model.put("societyEmail", enexseEmail);

                response = eesMailUtil.eesSendMail(user.get().getPersonalEmail(), model, verifyType);
            }
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
            return new ResponseEntity<Object>(new EesMessageResponse(
                    EesUserResponse.EES_USER_ERROR_SEND_EMAIL + email),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> sendCodeTwoFactoryAuthentification(String email, String code, String verifyType) {
        Optional<EesUser> user = null;
        Map<String, Object> model = new HashMap<>();
        CompletableFuture<EesGenericResponse> response = null;

        try {
            user = eesUserRepository.findByEnexseEmail(email);

            if (user.isPresent()) {
                model.put("name", user.get().getLastName().toUpperCase());
                model.put("email", user.get().getEnexseEmail());
                model.put("userId", user.get().getUserId().toUpperCase());
                model.put("code", code);
                response = eesMailUtil.eesSendMail(user.get().getEnexseEmail(), model, verifyType);
            }
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<Object>(new EesMessageResponse(
                    EesUserResponse.EES_USER_ERROR_SEND_EMAIL + email),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


