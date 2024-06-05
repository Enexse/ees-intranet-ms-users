package com.enexse.intranet.ms.users.payload.request;

import com.enexse.intranet.ms.users.enums.EesStatus;
import com.enexse.intranet.ms.users.models.partials.EesReferentCustomer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesCustomerRequest {

    private String customerCode;
    private String customerTitle;
    private String sectorField;
    private EesStatus status;
    private EesReferentCustomer referent;
    private String comments;
    private String landline;
    private String fax;
    private String website;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
}
