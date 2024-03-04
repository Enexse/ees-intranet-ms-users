package com.enexse.intranet.ms.users.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesFormationRequest {

    private String formationName;
    private String certification;
    private String organisme;
    private String startDate;
    private String endDate;
}
