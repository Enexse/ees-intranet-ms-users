package com.enexse.intranet.ms.users.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EesFrontEndLink {

    @Value("${application.url}")
    private String urlLink;

    public String getEesLinkUpdatePassword() {
        return urlLink + "/auth/update-password?linkChangePassword=";
    }

    public String getEesLinkCertifiedEmail() {
        return urlLink + "/auth/certification-email?linkCertifiedEmail=";
    }

}
