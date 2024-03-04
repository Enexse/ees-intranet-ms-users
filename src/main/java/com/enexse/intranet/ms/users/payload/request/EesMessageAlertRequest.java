package com.enexse.intranet.ms.users.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesMessageAlertRequest {

    private String alertTitle;
    private String alertDescription;
    private boolean alertStatus;
    private String createdBy;
}
