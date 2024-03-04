package com.enexse.intranet.ms.users.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesOrderNumberRequest {

    private String customerId;
    private String code;
    private String createdBy;
    private EesValidityRequest validity;
}
