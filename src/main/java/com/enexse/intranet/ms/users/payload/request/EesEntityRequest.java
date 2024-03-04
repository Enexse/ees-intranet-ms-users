package com.enexse.intranet.ms.users.payload.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesEntityRequest {

    @NonNull
    private String entityCode;
    private String entityDescription;
    private String createdBy;
}
