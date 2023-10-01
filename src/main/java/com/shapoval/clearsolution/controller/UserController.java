package com.shapoval.clearsolution.controller;


import com.shapoval.clearsolution.dto.DateRangeDto;
import com.shapoval.clearsolution.dto.EmailDto;
import com.shapoval.clearsolution.dto.UserDto;
import com.shapoval.clearsolution.dto.UserResponse;
import com.shapoval.clearsolution.mapper.UserMapper;
import com.shapoval.clearsolution.model.User;
import com.shapoval.clearsolution.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.net.URI;



@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    private  String location = "/api/v1/users/";


    @PostMapping
    public ResponseEntity<UserResponse<UserDto>> createUser(@Valid @RequestBody UserDto userDto){

            User user = userService.createUser(userMapper.toUser(userDto));

            return ResponseEntity.
                    created(URI.create(location + user.getId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(UserResponse.<UserDto>builder()
                            .data(userMapper.toDTO(user))
                            .path(location + user.getId())
                            .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse<UserDto>> updateUserEmail
            (@PathVariable Long id, @RequestBody @Valid EmailDto email){

        User user = userService.updateUserEmail(id, email.getEmail());

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserResponse.<UserDto>builder()
                        .data(userMapper.toDTO(user))
                        .path(location + user.getId())
                        .build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse<UserDto>> updateAllFieldsUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto){

         User user = userService.updateUser(id,userMapper.toUser(userDto));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserResponse.<UserDto>builder()
                        .data(userMapper.toDTO(user))
                        .path(location + user.getId())
                        .build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){

        userService.deleteUser(id);

        return ResponseEntity
                .noContent()
                .build();

    }

    @GetMapping( )
    private ResponseEntity<UserResponse<Page<UserDto>>> searchUsersByDateRange( @Valid @RequestBody DateRangeDto dateRange
            ,@RequestParam(name = "page", defaultValue = "0",required = false) int page
    ,@RequestParam(name = "size", defaultValue = "10",required = false) int size){
        Page<User> userPage = userService.searchUsersByBirthDateRange(dateRange.getFromDate(),dateRange.getToDate(), PageRequest.of(page, size));
        Page<UserDto> userDtoList = userMapper.toPageUserDto(userPage);


        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserResponse.<Page<UserDto>>builder()
                        .data(userDtoList)
                        .path("/api/v1/users")
                        .build());

    }

}
