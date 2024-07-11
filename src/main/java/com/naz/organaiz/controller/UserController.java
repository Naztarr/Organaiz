package com.naz.organaiz.controller;


import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.UserData;
import com.naz.organaiz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(
        name = "Users",
        description = "REST APIs for user related actions"
)
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{userID}")
    @Operation(summary = "user data",
            description = "Retrieves the record of a particular user")
    public ResponseEntity<ApiResponse<UserData>> getUserRecord(@PathVariable String userId){
        return userService.getUserRecord(userId);
    }
}
