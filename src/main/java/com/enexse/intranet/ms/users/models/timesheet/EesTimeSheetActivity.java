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
@Document(collection = "ees-timesheet-activities")
@Builder
public class EesTimeSheetActivity {

    @Id
    @GeneratedValue
    private String activityId;
    private String activityCode;
    private String activityDesignation;
    private String activityObservation;
    private String createdAt;
    private String updatedAt;
}
