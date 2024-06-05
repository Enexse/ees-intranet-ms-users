package com.enexse.intranet.ms.users.models;

import com.enexse.intranet.ms.users.enums.EesStatus;
import com.enexse.intranet.ms.users.models.partials.EesReferentCustomer;
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
@Document(collection = "ees-clients")
@Builder
public class EesCustomer {

    @Id
    private String customerId;

    private String customerCode;
    private String customerTitle;
    private String sectorField;
    private EesStatus status;

    @DBRef
    private EesReferentCustomer referent;

    private String comments;
    private String landline;
    private String fax;
    private String website;
    private String createdAt;
    private String updatedAt;
    private EesUser createdBy;
}
