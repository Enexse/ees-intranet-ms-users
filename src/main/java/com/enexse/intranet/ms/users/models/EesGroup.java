package com.enexse.intranet.ms.users.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-groups")
@Builder
public class EesGroup {

    @Id
    private String groupId;

    private String groupCode;
    private String groupTitle;
    private String groupDescription;
    private boolean groupTimesheet;

    @DBRef
    private List<EesUser> collaborators = new ArrayList<>();

    private String createdAt;
    private String updatedAt;

    @ToString.Exclude
    @DBRef
    @JsonManagedReference
    private EesUser createdBy;
}
