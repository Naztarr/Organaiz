package com.naz.organaiz.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Integer statusCode;

    public ApiResponse(String status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(String status, String message, Integer statusCode){
        this.status = status;
        this.message = message;
        this.statusCode = statusCode;
    }
    public ApiResponse(String status, String message){
        this.status = status;
        this.message = message;

    }
}
