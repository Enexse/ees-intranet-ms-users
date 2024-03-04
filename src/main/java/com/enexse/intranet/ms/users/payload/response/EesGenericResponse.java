package com.enexse.intranet.ms.users.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EesGenericResponse {

    private Object object;
    private String message;
}
