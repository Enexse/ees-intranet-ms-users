package com.enexse.intranet.ms.users.models.timesheet;

import lombok.Data;

@Data
public class EesTimeSheetWorkplace {

    private String workPlaceId;
    private String workPlaceCode;
    private String workPlaceDesignation;
    private String workPlaceObservation;
    private String createdAt;
    private String updatedAt;
}
