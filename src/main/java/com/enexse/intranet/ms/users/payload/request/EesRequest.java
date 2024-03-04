package com.enexse.intranet.ms.users.payload.request;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesRequest {

    @NonNull
    private String requestCode;
    private String requestTitle;
    private String createdBy;
}
