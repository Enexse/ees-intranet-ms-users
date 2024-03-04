package com.enexse.intranet.ms.users.constants;

import org.springframework.beans.factory.annotation.Value;

public class EesFrontEndLink {

    @Value("${application.url}")
    private static String urlLink = null;

    public static final String EES_LINK_UPDATE_PASSWORD = urlLink + "/auth/update-password?linkChangePassword=";
    public static final String EES_LINK_CERTIFIED_EMAIL = urlLink + "auth/certification-email?linkCertifiedEmail=";
}
