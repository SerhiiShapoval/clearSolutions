package com.shapoval.clearsolution.mapper;

import com.shapoval.clearsolution.dto.UserDto;
import com.shapoval.clearsolution.model.User;

public interface UserMapper {

    User toUser(UserDto userDto);
    UserDto toDTO(User user);


}
