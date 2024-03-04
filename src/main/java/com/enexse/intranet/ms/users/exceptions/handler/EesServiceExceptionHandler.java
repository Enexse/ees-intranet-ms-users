package com.enexse.intranet.ms.users.exceptions.handler;

import com.enexse.intranet.ms.users.exceptions.ObjectFoundException;
import com.enexse.intranet.ms.users.exceptions.ObjectNotFoundException;
import com.enexse.intranet.ms.users.payload.response.EesExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class EesServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = fieldErrors
                .stream()
                .map(err -> err.getField() + ":" + err.getDefaultMessage())
                .collect(Collectors.toList());

        EesExceptionResponse apiError = new EesExceptionResponse();
        apiError.setErrors(errors);
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setPath(request.getDescription(false));
        apiError.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ObjectFoundException.class})
    ResponseEntity<?> ObjectFoundHandler(Exception exception, ServletWebRequest request) {

        EesExceptionResponse apiError = new EesExceptionResponse();

        apiError.setTimestamp(LocalDateTime.now());
        apiError.setStatus(HttpStatus.FOUND);
        apiError.setErrors(Arrays.asList(exception.getMessage()));
        apiError.setPath(request.getDescription(true));

        return new ResponseEntity(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ObjectNotFoundException.class})
    ResponseEntity<?> ObjectNotFoundHandler(Exception exception, ServletWebRequest request) {

        EesExceptionResponse apiError = new EesExceptionResponse();

        apiError.setTimestamp(LocalDateTime.now());
        apiError.setStatus(HttpStatus.NOT_FOUND);
        apiError.setErrors(Arrays.asList(exception.getMessage()));
        apiError.setPath(request.getDescription(true));

        return new ResponseEntity(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
