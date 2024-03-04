package com.enexse.intranet.ms.users.models;

import com.enexse.intranet.ms.users.enums.EesStatusCustomer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-contracts")
@Builder
public class EesContract {

    @Id
    private String contractId;

    private String contractCode;
    private String contractTitle;
    private String contractDescription;
    private EesStatusCustomer status;
    private String createdAt;
    private String updatedAt;
    private EesUser createdBy;
}
