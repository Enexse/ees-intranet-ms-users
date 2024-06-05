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
public class EesMessageTypeRequest {

    private String code;
    private String title;
    private EesStatus status;
    private String description;
    private String createdBy;
}
