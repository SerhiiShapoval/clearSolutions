package com.shapoval.clearsolution.mapper;

import com.shapoval.clearsolution.dto.UserDto;
import com.shapoval.clearsolution.model.User;
import org.springframework.data.domain.Page;

public interface UserMapper {

    User toUser(UserDto userDto);
    UserDto toDTO(User user);

    Page<UserDto> toPageUserDto(Page<User> page);


}
