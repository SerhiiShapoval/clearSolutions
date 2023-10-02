package com.shapoval.clearsolution.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shapoval.clearsolution.dto.UserDto;
import com.shapoval.clearsolution.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapperImpl implements UserMapper {


    @Override
    public UserDto toDTO(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneList(user.getPhoneNumbers())
                .build();
    }

    @Override
    public Page<UserDto> toPageUserDto(List<User> userList, Pageable pageable) {
        List<UserDto> dtoPage = userList
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoPage,pageable,dtoPage.size());
    }

    @Override
    public User toUser(UserDto userDto) {

        return User.builder()
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .birthDate(userDto.getBirthDate())
                .address(userDto.getAddress())
                .phoneNumbers(userDto.getPhoneList())
                .build();
    }
}
