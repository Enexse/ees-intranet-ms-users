package com.enexse.intranet.ms.users.payload.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesMissionRequest {

    private String project;
    private String customerId;
    private String workplaceCode;
    private String startDate;
    private String endDate;
}
