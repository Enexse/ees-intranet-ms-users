package com.enexse.intranet.ms.users.payload.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesCreateRequest {

    @NonNull
    private String requestCode;
    private String userId;
    private String subRequestCode;
    private String recipientId;
    private String personalEmail;
    private String referentId;
    private String comments;

}
