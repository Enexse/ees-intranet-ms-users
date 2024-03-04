package com.enexse.intranet.ms.users.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesNewsRequest {

    private String newsTitle;
    private String newsDescription;
    private boolean newsStatus;
    private String createdAt;
    private String createdBy;
}
