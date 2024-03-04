package com.enexse.intranet.ms.users.models.timesheet;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-timesheet-contractHours")
@Builder
public class EesTimeSheetContractHours {

    @Id
    @GeneratedValue
    private String contactHoursId;
    private String contractHoursCode;
    private String contractHours;
    private String createdAt;
    private String updatedAt;
}
