package com.enexse.intranet.ms.users.payload.request;

import com.enexse.intranet.ms.users.enums.EesStatusRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesResponseManualRequest {

    private EesStatusRequest status;
    private String note;
    private String decline;
}
