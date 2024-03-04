package com.enexse.intranet.ms.users.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Document(collection = "ees-alert")
@Builder
public class EesMessageAlert {

    @Id
    private String alertId;
    private String alertTitle;
    private String alertDescription;
    private boolean alertStatus;
    private String createdAt;
    private String updatedAt;

    @DBRef
    @JsonManagedReference
    private EesUser createdBy;
}
