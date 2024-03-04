package com.enexse.intranet.ms.users.payload.request;

import com.enexse.intranet.ms.users.enums.EesStatusUser;
import com.enexse.intranet.ms.users.models.EesUserAddress;
import com.enexse.intranet.ms.users.models.EesUserContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesUserRequest {

    private String firstName;
    private String lastName;
    private String personalEmail;
    private String enexseEmail;
    private String password;
    private EesStatusUser status;
    private String gender;
    private String notes;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private String matricule;
    private EesUserAddress userAddress;
    private EesUserContact userContact;
    private String entityCode;
    private String departmentCode;
    private String professionCode;
}
