package com.naz.organaiz.exception;

import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.ValidationError;
import com.naz.organaiz.payload.ValidationErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class OrganaizExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrganaizException.class)
    public ResponseEntity<ApiResponse<String>> handleOrganaizException(Exception ex){
        ApiResponse response = new ApiResponse<>("Bad request", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                                   HttpStatusCode status, WebRequest request) {

        List<ValidationError> errors = ex.getBindingResult().getAllErrors().stream()
                .map(
                        error -> new ValidationError(((FieldError) error).getField(), error.getDefaultMessage())
                ).collect(Collectors.toList());
        ValidationErrorResponse response = new ValidationErrorResponse(422, errors);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
