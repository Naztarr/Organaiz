package com.naz.organaiz.service.serviceImplementation;

import com.naz.organaiz.exception.OrganaizException;
import com.naz.organaiz.mapper.UserMapper;
import com.naz.organaiz.model.User;
import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.UserData;
import com.naz.organaiz.repository.UserRepository;
import com.naz.organaiz.service.UserService;
import com.naz.organaiz.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserImplementation implements UserService {
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<ApiResponse<UserData>> getUserRecord(String id) {
        User currentUser = userRepository.findByEmail(UserUtil.getLoginUser()).orElseThrow(
                () -> new OrganaizException("User not found"));
        User userToView = userRepository.findById(id).orElseThrow(
                () -> new OrganaizException("User does not exist"));

            return (currentUser.getOrganisations().stream().anyMatch(
                    org -> userToView.getOrganisations().contains(org)) || currentUser.equals(userToView))?
                    new ResponseEntity<>(new ApiResponse<>("success",
                    "user record retrieved successfully",
                            UserMapper.mapToUserData(userToView, new UserData())), HttpStatus.OK) :
                    new ResponseEntity<>(new ApiResponse<>("Unauthorized",
                            "You're not authorized to view this information"),
                            HttpStatus.UNAUTHORIZED);
    }
}
