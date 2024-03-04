package com.enexse.intranet.ms.users.payload.request.timesheet;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesUserTimesheetRequest {

    @NonNull
    private int rtt;
    private String businessManagerType;
    private String businessManagerEnexse;
    private String businessManagerClient;
    private int numAffair;
    private String projectName;
    private String customerCode;
    private String activityCode;
    private String contractHoursCode;
    private String workplaceCode;
    private String entityCode;
    private String departmentCode;
    private String professionCode;
}
