package com.enexse.intranet.ms.users.models;

import com.enexse.intranet.ms.users.payload.request.EesPermissionAccessRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-user-permissions")
@Builder
public class
EesUserPermission {

    @Id
    private String permissionId;

    private String permissionCode;
    private String permissionTitle;
    private String permissionDescription;
    private String createdAt;
    private String updatedAt;
    private List<EesPermissionAccessRequest> permissionAccesses;
    @ToString.Exclude
    @DBRef(lazy = true)
    @JsonManagedReference
    private EesUser createdBy;
}
