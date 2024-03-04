package com.enexse.intranet.ms.users.payload.request.timesheet;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesTimeSheetContractHoursRequest {

    @NonNull
    private String contractHours;
}
