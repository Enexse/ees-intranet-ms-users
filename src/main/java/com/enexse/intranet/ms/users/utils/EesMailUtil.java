package com.enexse.intranet.ms.users.utils;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.payload.response.EesGenericResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class EesMailUtil {

    @Value("${spring.mail.username}")
    private String eesEmailSender;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration config;

    @Async
    public CompletableFuture<EesGenericResponse> eesSendMail(String email, Map<String, Object> model, String verifyType) {
        EesGenericResponse response = new EesGenericResponse();
        CompletableFuture<EesGenericResponse> future = new CompletableFuture<>();
        MimeMessage message = sender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment
            //helper.addAttachment("signature.png", new ClassPathResource("signature.png"));

            Template t = null;
            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_INVITATION_USER) == 0) {
                helper.setSubject(EesUserResponse.EES_SUBJECT_INVITATION_USER);
                t = config.getTemplate("invitation-user-template.ftl");
            }

            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_EMAIL_VERIFICATION) == 0) {
                helper.setSubject(String.format(EesUserResponse.EES_SUBJECT_CERTIFICATE_EMAIL, email));
                t = config.getTemplate("certification-email-template.ftl");
            }

            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_FORGOT_PASSWORD) == 0) {
                helper.setSubject(EesUserResponse.EES_SUBJECT_FORGOT_PASSWORD_USER);
                t = config.getTemplate("forgot-password-template.ftl");
            }

            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_UPDATE_REQUEST_STATUS) == 0) {
                helper.setSubject(EesUserResponse.EES_SUBJECT_UPDATE_REQUEST_STATUS);
                t = config.getTemplate("update-status-template.ftl");
            }

            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_2FACTORY_AUTH_CODE) == 0) {
                helper.setSubject(EesUserResponse.EES_SUBJECT_CODE_AUTHENTICATION);
                t = config.getTemplate("authentication-code-template.ftl");
            }

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(email);
            helper.setText(html, true);
            helper.setFrom(eesEmailSender);
            sender.send(message);

            response.setMessage(EesUserResponse.EES_MAIL_INVITATION_USER + email);
            response.setObject(Boolean.TRUE);
            future.complete(response);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending failure : " + e.getMessage());
            response.setObject(Boolean.FALSE);
            future.completeExceptionally(e);
        }
        return future;
    }

    @Async
    public CompletableFuture<EesGenericResponse> eesSendGroupMail(List<String> emails, List<String> ccEmails, Map<String, Object> model, String verifyType, String subject, String personalizedMessage, List<MultipartFile> attachments) {
        EesGenericResponse response = new EesGenericResponse();
        CompletableFuture<EesGenericResponse> future = new CompletableFuture<>();
        try {
            Template t = null;
            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_EMAIL_GROUP) == 0) {
                t = config.getTemplate("group-email-template.ftl");
            }
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Set the second parameter to true for multipart mode
            helper.setSubject(subject);
            helper.setText(personalizedMessage, true);
            helper.setFrom(new InternetAddress(eesEmailSender));
            for (String email : emails) {
                helper.addTo(email);
            }
            for (String ccEmail : ccEmails) {
                helper.addCc(ccEmail);
            }

            // Add the attachments to the email
            if (attachments != null) {
                long totalSize = 0;
                for (MultipartFile attachment : attachments) {
                    totalSize += attachment.getSize();
                    if (totalSize > 100 * 1024 * 1024) {
                        response.setMessage("Attachments size exceeds the limit of 100MB.");
                        response.setObject(Boolean.FALSE);
                        future.complete(response);
                        return future; // Return the error response
                    }
                    String attachmentFileName = attachment.getOriginalFilename();
                    InputStreamSource attachmentInputStreamSource = new InputStreamSource() {
                        @Override
                        public InputStream getInputStream() throws IOException {
                            return attachment.getInputStream();
                        }
                    };

                    helper.addAttachment(attachmentFileName, attachmentInputStreamSource);
                }
            }

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setText(html, true);
            sender.send(message);
            response.setMessage("Emails sent successfully to the group.");
            response.setObject(Boolean.TRUE);
            future.complete(response);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending failure: " + e.getMessage());
            response.setObject(Boolean.FALSE);
            future.completeExceptionally(e);
        }
        return future;
    }


    @Async
    public CompletableFuture<EesGenericResponse> eesSendCollaboratorMail(String email, Map<String, Object> model, String verifyType, String subject, String personalizedMessage, List<MultipartFile> attachments) {
        EesGenericResponse response = new EesGenericResponse();
        CompletableFuture<EesGenericResponse> future = new CompletableFuture<>();
        try {
            Template t = null;
            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_EMAIL_COLLABORATOR) == 0) {
                t = config.getTemplate("collaborator-email-template.ftl");
            }
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Set the second parameter to true for multipart mode
            helper.setSubject(subject);
            helper.setText(personalizedMessage, true);
            helper.setFrom(new InternetAddress(eesEmailSender));

            helper.addTo(email);

            // Add the attachments to the email
            if (attachments != null) {
                long totalSize = 0;
                for (MultipartFile attachment : attachments) {
                    totalSize += attachment.getSize();
                    if (totalSize > 100 * 1024 * 1024) {
                        response.setMessage("Attachments size exceeds the limit of 100MB.");
                        response.setObject(Boolean.FALSE);
                        future.complete(response);
                        return future; // Return the error response
                    }
                    String attachmentFileName = attachment.getOriginalFilename();
                    InputStreamSource attachmentInputStreamSource = new InputStreamSource() {
                        @Override
                        public InputStream getInputStream() throws IOException {
                            return attachment.getInputStream();
                        }
                    };

                    helper.addAttachment(attachmentFileName, attachmentInputStreamSource);
                }
            }

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setText(html, true);
            sender.send(message);
            response.setMessage("Emails sent successfully to the group.");
            response.setObject(Boolean.TRUE);
            future.complete(response);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending failure: " + e.getMessage());
            response.setObject(Boolean.FALSE);
            future.completeExceptionally(e);
        }
        return future;
    }


    public CompletableFuture<EesGenericResponse> eesSendMailWithAttachement(String email, Map<String, Object> model, String verifyType, List<MultipartFile> attachments) {
        EesGenericResponse response = new EesGenericResponse();
        CompletableFuture<EesGenericResponse> future = new CompletableFuture<>();
        MimeMessage message = sender.createMimeMessage();

        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template t = null;
            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_INVITATION_USER) == 0) {
                helper.setSubject(EesUserResponse.EES_SUBJECT_INVITATION_USER);
                t = config.getTemplate("invitation-user-template.ftl");
            }

            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_EMAIL_VERIFICATION) == 0) {
                helper.setSubject(String.format(EesUserResponse.EES_SUBJECT_CERTIFICATE_EMAIL, email));
                t = config.getTemplate("certification-email-template.ftl");
            }

            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_FORGOT_PASSWORD) == 0) {
                helper.setSubject(EesUserResponse.EES_SUBJECT_FORGOT_PASSWORD_USER);
                t = config.getTemplate("forgot-password-template.ftl");
            }

            if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_UPDATE_REQUEST_STATUS) == 0) {
                helper.setSubject(EesUserResponse.EES_SUBJECT_UPDATE_REQUEST_STATUS);
                t = config.getTemplate("update-status-template.ftl");
            }

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(email);
            helper.setText(html, true);
            helper.setFrom(eesEmailSender);

            // Add attachments to the email
            if (attachments != null) {
                long totalSize = 0;
                for (MultipartFile attachment : attachments) {
                    totalSize += attachment.getSize();
                    if (totalSize > 100 * 1024 * 1024) {
                        response.setMessage("Attachments size exceeds the limit of 100MB.");
                        response.setObject(Boolean.FALSE);
                        future.complete(response);
                        return future;
                    }
                    String attachmentFileName = attachment.getOriginalFilename();
                    InputStreamSource attachmentInputStreamSource = new InputStreamSource() {
                        @Override
                        public InputStream getInputStream() throws IOException {
                            return attachment.getInputStream();
                        }
                    };
                    helper.addAttachment(attachmentFileName, attachmentInputStreamSource);
                }
            }

            sender.send(message);

            response.setMessage(EesUserResponse.EES_MAIL_INVITATION_USER + email);
            response.setObject(Boolean.TRUE);
            future.complete(response);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending failure : " + e.getMessage());
            response.setObject(Boolean.FALSE);
            future.completeExceptionally(e);
        }

        return future;
    }
}

