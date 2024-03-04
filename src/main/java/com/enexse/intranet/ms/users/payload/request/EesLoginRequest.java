package com.enexse.intranet.ms.users.payload.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesLoginRequest {

    @NonNull
    private String email;
    private String password;
}
