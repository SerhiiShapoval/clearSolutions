package com.shapoval.clearsolution.web.controller;



import com.shapoval.clearsolution.web.dto.PageResponse;
import com.shapoval.clearsolution.web.dto.DateRangeDto;
import com.shapoval.clearsolution.web.dto.EmailDto;
import com.shapoval.clearsolution.web.dto.UserDto;
import com.shapoval.clearsolution.web.dto.UserResponse;
import com.shapoval.clearsolution.web.mapper.UserMapper;
import com.shapoval.clearsolution.model.User;
import com.shapoval.clearsolution.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import javax.validation.constraints.Positive;

import java.net.URI;



@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper = UserMapper.mapper;

    private String location = "/api/v1/users/";


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse<UserDto>> userById(@Positive @PathVariable Long id){

        return ResponseEntity
                .ok()
                .body(UserResponse.<UserDto>builder()
                        .data(userMapper.toDTO(userService.findUserById(id)))
                        .path(location + id)
                        .build());

    }

    @PostMapping
    public ResponseEntity<UserResponse<UserDto>> createUser( @Valid @RequestBody UserDto userDto){

            User user = userService.createUser(userMapper.toUser(userDto));

            return ResponseEntity.
                    created(URI.create(location + user.getId()))
                    .body(UserResponse.<UserDto>builder()
                            .data(userMapper.toDTO(user))
                            .path(location + user.getId())
                            .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse<UserDto>> updateUserEmail
            (@PathVariable @Positive Long id, @Valid @RequestBody  EmailDto email){

        User user = userService.updateUserEmail(id, email.getEmail());

        return ResponseEntity
                .ok()
                .body(UserResponse.<UserDto>builder()
                        .data(userMapper.toDTO(user))
                        .path(location + user.getId())
                        .build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse<UserDto>> updateAllFieldsUser(@PathVariable @Positive Long id
            , @Valid @RequestBody UserDto userDto){

         User user = userService.updateUser(id,userMapper.toUser(userDto));

        return ResponseEntity.ok()
                .body(UserResponse.<UserDto>builder()
                        .data(userMapper.toDTO(user))
                        .path(location + user.getId())
                        .build());

    }

    @DeleteMapping("/{id}")

    public ResponseEntity<?> deleteUser(  @PathVariable @Positive Long id){

        userService.deleteUser(id);

        return ResponseEntity
                .noContent()
                .build();

    }

    @GetMapping()
    public ResponseEntity<UserResponse<PageResponse<UserDto>>> searchUsersByDateRange(
            @Valid @RequestBody DateRangeDto dateRange
            , @RequestParam(name = "page", defaultValue = "0",required = false) int page
            ,  @RequestParam(name = "size", defaultValue = "10",required = false) int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<UserDto> userDtoList =
                userService.searchUsersByBirthDateRange(dateRange.getFromDate(),dateRange.getToDate(),pageable)
                        .map(userMapper::toDTO);

        return ResponseEntity
                .ok()
                .body(UserResponse.<PageResponse<UserDto>>builder()
                        .data(new PageResponse<>(userDtoList.getContent(),userDtoList.getTotalElements(),pageable))
                        .path(location)
                        .build());

    }

}
