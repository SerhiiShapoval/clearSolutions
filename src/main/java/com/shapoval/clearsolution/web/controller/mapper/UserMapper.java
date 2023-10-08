package com.shapoval.clearsolution.web.controller.mapper;

import com.shapoval.clearsolution.web.controller.dto.UserDto;
import com.shapoval.clearsolution.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;


@Mapper
public interface UserMapper {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    User toUser(UserDto userDto);
    UserDto toDTO(User user);

}
