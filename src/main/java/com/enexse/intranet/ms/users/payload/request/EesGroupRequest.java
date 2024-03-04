package com.enexse.intranet.ms.users.payload.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesGroupRequest {

    @NonNull
    private String groupCode;
    private String groupTitle;
    private String groupDescription;
    private List<String> collaboratorsIds;
    private String createdBy;
    private boolean groupTimesheet;
}
