package com.naz.organaiz.mapper;

import com.naz.organaiz.dto.UserDto;
import com.naz.organaiz.model.User;
import com.naz.organaiz.payload.UserData;

public class UserMapper {

    public static User mapToUser(UserDto dto, User user){
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());

        return user;
    }
    public static UserData mapToUserData(User user, UserData userData){
        userData.setUserId(user.getUserId());
        userData.setFirstName(user.getFirstName());
        userData.setLastName(user.getLastName());
        userData.setEmail(user.getEmail());
        userData.setPhone(user.getPhone());

        return userData;
    }
}
