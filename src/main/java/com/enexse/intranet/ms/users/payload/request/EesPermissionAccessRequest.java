package com.enexse.intranet.ms.users.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EesPermissionAccessRequest {

    private String permissionCode;
    private Boolean read;
    private Boolean write;
    private Boolean create;
}
