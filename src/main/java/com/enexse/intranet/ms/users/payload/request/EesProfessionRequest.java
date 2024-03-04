package com.enexse.intranet.ms.users.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesProfessionRequest {

    private String professionCode;
    private String professionName;
    private String departmentCode;
    private String createdBy;
}
