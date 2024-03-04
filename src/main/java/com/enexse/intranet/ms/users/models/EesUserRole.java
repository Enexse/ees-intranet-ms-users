package com.enexse.intranet.ms.users.models;

import com.enexse.intranet.ms.users.enums.EesKeycloakRoles;
import com.enexse.intranet.ms.users.payload.request.EesPermissionAccessRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-user-roles")
@Builder
public class EesUserRole {

    @Id
    private String roleId;
    private String roleCode;
    private String roleTitle;
    private String roleDescription;
    private String keycloakRole;

    @DBRef
    private List<EesUserPermission> permissions;

    private List<String> users = new ArrayList<>();

    @DBRef
    private EesUserRolePermission accesses;

    private List<EesPermissionAccessRequest> permissionAccessRequest;

    @Override
    public String toString() {
        return "EesUserRole{" +
                "roleId='" + roleId + '\'' +
                "roleCode='" + keycloakRole + '\'' +
                "roleDescription='" + roleDescription + '\'' +
                '}';
    }
}
