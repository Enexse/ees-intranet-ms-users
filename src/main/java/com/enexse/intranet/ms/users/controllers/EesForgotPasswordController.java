package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesVerifyIdentity;
import com.enexse.intranet.ms.users.payload.request.EesUserEmailRequest;
import com.enexse.intranet.ms.users.services.EesUserService;
import com.enexse.intranet.ms.users.services.EesVerifyIdentityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesForgotPasswordController {

    private EesVerifyIdentityService eesVerifyIdentityService;
    private EesUserService eesUserService;

    @GetMapping(EesUserEndpoints.EES_GET_LINK)
    public Optional<EesVerifyIdentity> eesGetIdentityByLinkId(@RequestParam String link) {
        return eesVerifyIdentityService.getIdentityByLink(link);
    }

    @PostMapping(EesUserEndpoints.EES_EMAIL_USER)
    public ResponseEntity<Object> eesGetForgotPasswordLink(@RequestBody EesUserEmailRequest request) {
        if (!eesUserService.verifyPersonalEmail(request.getEmail())) {
            return eesVerifyIdentityService.getForgotPasswordLink(request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(EesUserEndpoints.EES_UPDATE_EXPIRY_DATE_LINK)
    public ResponseEntity<Object> eesUpdateExpiryDateLink(@PathVariable String link,
                                                          @RequestParam(value = "email", required = true) String email,
                                                          @RequestParam(value = "verifyType", required = true) String verifyType) {
        return eesVerifyIdentityService.updateExpiryDateLink(link, email, verifyType);
    }

    @GetMapping(EesUserEndpoints.EES_VERIFY_LINK_EXISTS)
    public ResponseEntity<Object> eesVerifyLinkExistsByLink(@RequestParam String link) {
        return eesVerifyIdentityService.verifyLinkExistsBylink(link);
    }
}
