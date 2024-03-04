package com.enexse.intranet.ms.users.payload.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class EesCreateUserRequest {

    @NonNull
    private String firstName;
    private String lastName;
    private String email;
    private String roleCode;
}
