package com.naz.organaiz.controller;


import com.naz.organaiz.dto.LoginDto;
import com.naz.organaiz.dto.UserDto;
import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.UserResponse;
import com.naz.organaiz.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "REST APIs for user registration and login"
)
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "signup",
            description = "For signup and creating a user")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody @Validated UserDto userDto){
        return authenticationService.signup(userDto);
    }

    @PostMapping("/login")
    @Operation(summary = "login",
            description = "Authenticates the user with email and password")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginDto loginDto){
        return authenticationService.login(loginDto);
    }

}
