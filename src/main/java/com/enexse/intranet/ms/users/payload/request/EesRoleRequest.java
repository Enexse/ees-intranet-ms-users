package com.enexse.intranet.ms.users.payload.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesRoleRequest {

    @NonNull
    public String roleCode;
    public String roleDescription;
    public String roleTitle;
    public String keycloakRole;
    private List<EesPermissionAccessRequest> permissionAccesses;

}
