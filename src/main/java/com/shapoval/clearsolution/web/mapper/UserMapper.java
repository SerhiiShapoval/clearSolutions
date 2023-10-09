package com.shapoval.clearsolution.web.mapper;

import com.shapoval.clearsolution.web.dto.UserDto;
import com.shapoval.clearsolution.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    User toUser(UserDto userDto);
    UserDto toDTO(User user);

}
