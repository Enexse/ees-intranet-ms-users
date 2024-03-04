package com.enexse.intranet.ms.users.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-user-rolePermission")
@Builder

public class EesUserRolePermission {


    @Id
    @GeneratedValue
    private String accessId;

    private String roleCode;
    private List<String> permissionCode;
}
