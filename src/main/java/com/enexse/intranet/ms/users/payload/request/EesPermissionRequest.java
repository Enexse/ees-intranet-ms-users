package com.enexse.intranet.ms.users.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesPermissionRequest {

    private String permissionCode;
    private String permissionTitle;
    private String permissionDescription;
    private Date createdAt;
    private Date updatedAt;
    private boolean read;
    private boolean write;
    private boolean create;
    private String createdBy;

}
