package com.shapoval.clearsolution.mapper;

import com.shapoval.clearsolution.dto.UserDto;
import com.shapoval.clearsolution.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserMapper {

    User toUser(UserDto userDto);
    UserDto toDTO(User user);

    Page<UserDto> toPageUserDto(List<User> userList, Pageable pageable);


}
