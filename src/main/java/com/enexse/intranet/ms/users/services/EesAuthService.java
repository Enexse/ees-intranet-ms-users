package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesVerifyIdentity;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.repositories.EesVerifyIdentityRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EesAuthService {

    private EesUserRepository eesUserRepository;
    private EesVerifyIdentityRepository eesVerifyIdentityRepository;

    public boolean isValidUser(String pseudo, String userId) {
        Optional<EesUser> user = eesUserRepository.findByPseudoAndUserId(pseudo, userId);
        return user.isEmpty();
    }

    public ResponseEntity<Object> verifyLinkUser(String link, String verifyType) {
        Optional<EesVerifyIdentity> user = eesVerifyIdentityRepository.findByLinkAndVerifyType(link, verifyType);
        if (user.isPresent()) {
            return new ResponseEntity<Object>(user,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, link)), HttpStatus.NOT_FOUND);
        }
    }
}
