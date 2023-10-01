package com.shapoval.clearsolution.mapper;

import com.shapoval.clearsolution.dto.UserDto;
import com.shapoval.clearsolution.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Page<UserDto> toPageUserDto(Page<User> page) {
        List<UserDto> dtoPage = page.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoPage,page.getPageable(),page.getTotalElements());
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
