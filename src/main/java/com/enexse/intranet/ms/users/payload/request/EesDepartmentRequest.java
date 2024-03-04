package com.enexse.intranet.ms.users.payload.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesDepartmentRequest {

    @NonNull
    private String departmentCode;
    private String departmentDescription;
    private String createdBy;
}
