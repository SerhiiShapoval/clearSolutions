package com.shapoval.clearsolution.controller;


import com.shapoval.clearsolution.dto.DateRangeDto;
import com.shapoval.clearsolution.dto.UserDto;
import com.shapoval.clearsolution.dto.UserResponse;
import com.shapoval.clearsolution.mapper.UserMapper;
import com.shapoval.clearsolution.model.User;
import com.shapoval.clearsolution.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;



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
    public ResponseEntity<UserResponse<UserDto>> updateUserEmail(@PathVariable Long id, @RequestBody String email){

        User user = userService.updateUserEmail(id,email);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserResponse.<UserDto>builder()
                        .data(userMapper.toDTO(user))
                        .path(location + user.getId())
                        .build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse<UserDto>> updateAllFieldsUser(@PathVariable Long id, @RequestBody UserDto userDto){

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
    private ResponseEntity<UserResponse<List<UserDto>>> searchUsersByDateRange( @Valid @RequestBody DateRangeDto dateRange){
        List<UserDto> userDtoList =
                 userService.searchUsersByBirthDateRange(dateRange.getFromDate(),dateRange.getToDate())
                .stream()
                .map(userMapper::toDTO)
                .toList();

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserResponse.<List<UserDto>>builder()
                        .data(userDtoList)
                        .path("/api/v1/users")
                        .build());

    }

}
