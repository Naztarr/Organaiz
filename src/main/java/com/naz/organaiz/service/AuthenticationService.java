package com.naz.organaiz.service;

import com.naz.organaiz.dto.LoginDto;
import com.naz.organaiz.dto.UserDto;
import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.UserResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<ApiResponse<UserResponse>> signup(UserDto dto);
    ResponseEntity<ApiResponse<UserResponse>> login(LoginDto dto);
}
