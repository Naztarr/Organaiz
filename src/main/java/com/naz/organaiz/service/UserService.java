package com.naz.organaiz.service;

import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.UserData;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse<UserData>> getUserRecord(String id);

}
