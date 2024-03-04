package com.enexse.intranet.ms.users.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-entities")
@Builder
public class EesUserEntity {

    @Id
    private String entityId;

    private String entityCode;
    private String entityDescription;
    private String createdAt;
    private String updatedAt;

    @ToString.Exclude
    @DBRef
    @JsonManagedReference
    private EesUser createdBy;

}
