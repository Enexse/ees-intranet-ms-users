package com.enexse.intranet.ms.users.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-professions")
@Builder
public class EesUserProfession {

    @Id
    private String professionId;

    private String professionCode ;
    private String professionName;
    private String departmentCode;
    private String departmentDescription;

    private String createdAt;
    private String updatedAt;

    @ToString.Exclude
    @DBRef
    @JsonManagedReference
    private EesUser createdBy;
}

