package com.enexse.intranet.ms.users.controllers;

import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.payload.request.EesUserAuthRequest;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.services.EesAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesAuthController {

    private EesAuthService authService;
    private EesUserRepository eesUserRepository;


    @PostMapping(EesUserEndpoints.EES_AUTH_USER)
    public ResponseEntity<EesUser> eesLogin(@RequestBody @Valid EesUserAuthRequest credentials) {
        String pseudo = credentials.getPseudo();
        String userId = credentials.getUserId();

        boolean isValidUser = authService.isValidUser(pseudo, userId);
        if (!isValidUser) {
            // Successful login
            Optional<EesUser> userOpt = eesUserRepository.findByPseudoAndUserId(pseudo, userId);
            EesUser user = userOpt.get();
            return ResponseEntity.ok(user);
        } else {
            // Invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping(EesUserEndpoints.EES_VERIFY_LINK_USER)
    public ResponseEntity<Object> eesVerifyLink(
            @RequestParam(value = "link", required = true) String link,
            @RequestParam(value = "verifyType", required = true) String verifyType) {
        return authService.verifyLinkUser(link, verifyType);
    }
}
