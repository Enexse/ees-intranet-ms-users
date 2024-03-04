package com.enexse.intranet.ms.users.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-verify-identities")
@Builder
public class EesVerifyIdentity {

    @Id
    private String id;

    private String expiry_date;
    private String verifyType;
    private String userEmail;
    private String userId;
    private String link;
    private String code;
}
