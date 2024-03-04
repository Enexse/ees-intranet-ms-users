package com.enexse.intranet.ms.users.models.timesheet;

import com.enexse.intranet.ms.users.models.EesCustomer;
import com.enexse.intranet.ms.users.models.EesUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-users-timesheet")
@Builder
public class EesUserTimesheet {

    @Id
    private String userTimesheetId;

    @DBRef(lazy = true)
    @JsonBackReference
    private EesUser user;

    private int rtt;
    private String businessManagerType;
    private String businessManagerEnexse;
    private String businessManagerClient;
    private int numAffair;
    private String projectName;

    @DBRef
    private EesCustomer eesCustomer;

    @DBRef
    private EesTimeSheetActivity timeSheetActivity;

    @DBRef
    private EesTimeSheetContractHours contractHours;

    @DBRef
    private EesTimeSheetWorkplace workplace;
}
