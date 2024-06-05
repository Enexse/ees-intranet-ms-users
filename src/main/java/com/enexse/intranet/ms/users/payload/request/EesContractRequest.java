package com.enexse.intranet.ms.users.payload.request;

import com.enexse.intranet.ms.users.enums.EesStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesContractRequest {

    private String contractCode;
    private String contractTitle;
    private String contractDescription;
    private EesStatus status;
    private String createdBy;
}
