package com.enexse.intranet.ms.users.payload.request.timesheet;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesTimeSheetWorkPlaceRequest {
    private String workPlaceCode;
    private String workPlaceDesignation;
    private String workPlaceObservation;
}
