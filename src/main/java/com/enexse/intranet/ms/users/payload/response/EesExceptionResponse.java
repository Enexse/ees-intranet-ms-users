package com.enexse.intranet.ms.users.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EesExceptionResponse {

    private HttpStatus status;
    private List<String> errors;
    private LocalDateTime timestamp;
    private String path;
}
