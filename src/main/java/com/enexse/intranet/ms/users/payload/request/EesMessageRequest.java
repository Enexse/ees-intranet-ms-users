package com.enexse.intranet.ms.users.payload.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesMessageRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String subject;
    private String message;
}
