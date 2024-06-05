package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesFrontEndLink;
import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesVerifyIdentity;
import com.enexse.intranet.ms.users.payload.request.EesUserEmailRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.repositories.EesVerifyIdentityRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import com.enexse.intranet.ms.users.utils.EesMailUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class EesVerifyIdentityService {

    private EesUserRepository eesUserRepository;
    private EesVerifyIdentityRepository eesVerifyIdentityRepository;
    private EesMailUtil eesMailUtil;
    private EesUserService eesUserService;
    private EesMailService eesMailService;
    private EesFrontEndLink eesFrontEndLink;

    public ResponseEntity<Object> getForgotPasswordLink(EesUserEmailRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        Optional<EesUser> user = null;
        Optional<List<EesVerifyIdentity>> list = null;
        Map<String, Object> model = new HashMap<>();

        try {
            // First find user with personal email
            user = eesUserRepository.findByPersonalEmail(request.getEmail());
            if (!user.isPresent()) {
                // If user not present, then find wit enexse email
                user = eesUserService.getUserByEnexseEmail(request.getEmail());
            }
            if (user.isPresent()) {
                list = eesVerifyIdentityRepository.findByUserId(user.get().getUserId());
                if (list.isPresent()) {
                    for (EesVerifyIdentity link : list.get()) {
                        LocalDateTime date1 = LocalDateTime.parse(link.getExpiry_date(), formatter);
                        LocalDateTime date2 = LocalDateTime.parse(EesCommonUtil.generateCurrentDateUtil(), formatter);
                        if (date1.isAfter(date2)) {
                            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_LINK_ALREADY_SENT), HttpStatus.BAD_REQUEST);
                        }
                    }
                }
                EesVerifyIdentity verifyIdentity = new EesVerifyIdentity()
                        .builder()
                        .expiry_date(EesCommonUtil.generateExpirationLink())
                        .userEmail(request.getEmail())
                        .verifyType(EesUserConstants.EES_VERIFY_TYPE_FORGOT_PASSWORD)
                        .userId(user.get().getUserId())
                        .link(UUID.randomUUID().toString())
                        .build();
                eesVerifyIdentityRepository.save(verifyIdentity);
                String link = eesFrontEndLink.getEesLinkUpdatePassword() + verifyIdentity.getLink() + "&expirationDate=" + verifyIdentity.getExpiry_date() + "&verifyType="
                        + verifyIdentity.getVerifyType();
                link = link.replaceAll("\\s", "");
                System.out.println(link);
                ResponseEntity<Object> response = eesMailService.sendForgotPasswordLinkToUserEmail(user, request.getEmail(), link, EesUserConstants.EES_VERIFY_TYPE_FORGOT_PASSWORD);
                if (response.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_FORGOT_PASSWORD_LINK, request.getEmail())), HttpStatus.OK);
                }
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_ERROR_SEND_EMAIL + request.getEmail()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getEmail())), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_ERROR_SEND_EMAIL + request.getEmail()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<EesVerifyIdentity> getIdentityByLink(String link) {
        Optional<EesVerifyIdentity> identity = eesVerifyIdentityRepository.findByLink(link);
        return identity;
    }

    public ResponseEntity<Object> verifyLinkExistsBylink(String link) {
        Optional<EesVerifyIdentity> identity = eesVerifyIdentityRepository.findByLink(link);
        if (identity.isEmpty()) {
            return ResponseEntity.ok(false);
        } else {
            return ResponseEntity.ok(true);
        }
    }

    public ResponseEntity<Object> updateExpiryDateLink(String link, String email, String verifyType) {
        Optional<EesUser> user = null;
        Map<String, Object> model = new HashMap<>();
        String urlLink = "";
        ResponseEntity<Object> response = null;

        try {
            // First find user with personal email
            user = eesUserRepository.findByPersonalEmail(email);
            if (!user.isPresent()) {
                // If user not present, then find wit enexse email
                user = eesUserService.getUserByEnexseEmail(email);
            }
            if (user.isPresent()) {
                Optional<EesVerifyIdentity> identity = null;
                try {
                    identity = eesVerifyIdentityRepository.findByLink(link);
                    if (identity.isPresent()) {
                        identity.get().setExpiry_date(EesCommonUtil.generateExpirationLink());
                        eesVerifyIdentityRepository.save(identity.get());
                        if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_FORGOT_PASSWORD) == 0) {
                            urlLink = eesFrontEndLink.getEesLinkUpdatePassword() + identity.get().getLink() + "&expirationDate=" + identity.get().getExpiry_date() + "&verifyType="
                                    + identity.get().getVerifyType();
                            response = eesMailService.sendForgotPasswordLinkToUserEmail(user, email, urlLink, EesUserConstants.EES_VERIFY_TYPE_FORGOT_PASSWORD);
                        }
                        if (verifyType.compareToIgnoreCase(EesUserConstants.EES_VERIFY_TYPE_EMAIL_VERIFICATION) == 0) {
                            urlLink = eesFrontEndLink.getEesLinkCertifiedEmail() + identity.get().getLink() + "&expirationDate=" + identity.get().getExpiry_date() + "&verifyType="
                                    + identity.get().getVerifyType();
                            response = eesMailService.certificationUserEmail(email, urlLink, EesUserConstants.EES_VERIFY_TYPE_EMAIL_VERIFICATION);
                        }
                        //urlLink = urlLink.replaceAll("\\s", "");
                        System.out.println(urlLink);
                        if (response.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) {
                            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_FORGOT_PASSWORD_LINK, email)), HttpStatus.OK);
                        }
                        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_ERROR_SEND_EMAIL + email), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_LINK_NOT_FOUND)), HttpStatus.NOT_FOUND);
                } catch (Exception ex) {
                    return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_ERROR_SEND_LINK + identity.get().getUserEmail()), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, email)), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_ERROR_SEND_EMAIL + email), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
