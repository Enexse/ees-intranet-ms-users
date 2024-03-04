package com.enexse.intranet.ms.users.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesUpdateUserProfile {

    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String addressLine;
    private String zipCode;
    private String phoneNumber;
    private String prefixPhone;

}
