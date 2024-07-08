package com.naz.organaiz.service.serviceImplementation;


import com.naz.organaiz.dto.LoginDto;
import com.naz.organaiz.dto.UserDto;
import com.naz.organaiz.exception.OrganaizException;
import com.naz.organaiz.mapper.UserMapper;
import com.naz.organaiz.model.Organisation;
import com.naz.organaiz.model.User;
import com.naz.organaiz.payload.ApiResponse;
import com.naz.organaiz.payload.UserResponse;
import com.naz.organaiz.payload.UserData;
import com.naz.organaiz.repository.OrganisationRepository;
import com.naz.organaiz.repository.UserRepository;
import com.naz.organaiz.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthImplementation implements AuthenticationService {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtImplementation jwtImplementation;
    private final Long expire = 900000L;
    protected String generateToken(User user, Long expiryDate){
        Map<String, Object> claims = new HashMap<>();
        claims.put("first_name",user.getFirstName());
        claims.put("last_name", user.getLastName());
        return jwtImplementation.generateJwtToken(claims, user.getEmail(), expiryDate);
    }
    @Override
    public ResponseEntity<ApiResponse<UserResponse>> signup(UserDto dto) {
        User user = new User();
        UserResponse userResponse = new UserResponse();

        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        if(userOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse<>("Bad request", "A user already exists with this email",
                    422), HttpStatus.UNPROCESSABLE_ENTITY);
        } else if(dto.password().equals(dto.confirmPassword())){
            UserMapper.mapToUser(dto, user);
            user.setPassword(passwordEncoder.encode(dto.password()));

            //Creates an organisation for the user
            Organisation organisation = new Organisation();
            organisation.setName(String.format("%s's Organisation", dto.firstName()));
            Organisation org = organisationRepository.save(organisation);

            //Includes the organisation to the list of organisations the user created or belongs to
            user.getOrganisations().add(org);
            User savedUser = userRepository.save(user);
            organisation.getUsers().add(savedUser);



            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            userResponse.setAccessToken(generateToken(user, null));
            userResponse.setUserData(UserMapper.mapToUserData(user, new UserData()));
        } else {
            return new ResponseEntity<>(new ApiResponse<>("Bad request", "Registration unsuccessful",
                    400), HttpStatus.BAD_REQUEST);
        }
        ApiResponse response = new ApiResponse<>("success", "Registration successful", userResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse<UserResponse>> login(LoginDto dto) {
        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        if(userOptional.isPresent()){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserResponse userResponse = new UserResponse();
            userResponse.setAccessToken(generateToken(userOptional.get(), null));
            userResponse.setUserData(UserMapper.mapToUserData(userOptional.get(), new UserData()));

            return new ResponseEntity<>(new ApiResponse<>("success",
                    "Login successful", userResponse), HttpStatus.OK);

        } else{
            return new ResponseEntity<>(new ApiResponse<>("Bad request",
                    "Authentication failed", 401), HttpStatus.UNAUTHORIZED);
        }
    }
}
