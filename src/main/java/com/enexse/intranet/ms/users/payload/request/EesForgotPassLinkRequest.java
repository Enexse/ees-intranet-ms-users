package com.enexse.intranet.ms.users.payload.request;

import com.enexse.intranet.ms.users.models.EesUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesForgotPassLinkRequest {

    private String expiry_date;
    private String verify_type;
    private String userEmail;
    private String userId;
    private String link;
}
