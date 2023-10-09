package com.shapoval.clearsolution.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapoval.clearsolution.web.dto.DateRangeDto;
import com.shapoval.clearsolution.web.dto.EmailDto;
import com.shapoval.clearsolution.web.dto.UserDto;
import com.shapoval.clearsolution.error.UserEmailExistException;
import com.shapoval.clearsolution.error.UserNotFoundException;
import com.shapoval.clearsolution.error.UserWrongAgeException;
import com.shapoval.clearsolution.error.UserWrongDateException;
import com.shapoval.clearsolution.web.mapper.UserMapper;
import com.shapoval.clearsolution.model.User;
import com.shapoval.clearsolution.service.UserService;

import com.shapoval.clearsolution.web.controller.UserController;
import com.shapoval.clearsolution.web.mapper.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private User testUser;
    private UserDto testUserDto;
    private DateRangeDto dateRangeDto;
    private List<User> list;

    private List<UserDto> listDto;

    private Page<UserDto> listPageDto;
    private Page<User> pageUser;
    private Pageable pageable;
    private EmailDto emailDto;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

   @MockBean
    private UserMapper userMapper ;

    @BeforeEach
    void setUp(){



        testUser = new User(1L,"example@gmail.com","Serhii",
                LocalDate.of(1991,1,13),"Shapoval",null,null);

        testUserDto =  new UserDto("example@gmail.com","Serhii","Shapoval",
                LocalDate.of(1991,1,13),null,null);

        LocalDate from = LocalDate.of(1990,1,1);
        LocalDate to = LocalDate.of(2023,9,30);

        dateRangeDto = new DateRangeDto(from,to);
        pageable = PageRequest.of(0,10);
        list = new ArrayList<>();
        listDto = new ArrayList<>();
        list.add(testUser);
        listDto.add(testUserDto);
        listPageDto = new PageImpl<>(listDto,pageable,listDto.size());

        emailDto = new EmailDto("example123@gmail.com");
        pageUser = new PageImpl<>(list,pageable, list.size());

//        when(userMapper.toUser(any(UserDto.class))).thenReturn(testUser);
//        when(userMapper.toDTO(any(User.class))).thenReturn(testUserDto);

    }

    @Test
    void createUser_ReturnUser200Ok() throws Exception {

        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.firstName").value("Serhii"))
                .andExpect(jsonPath("$.data.email").value("example@gmail.com"))
                .andExpect(header().string("Location","/api/v1/users/1"));

        verify(userService, times(1)).createUser(any(User.class));

    }
    @Test
    void createUser_ByExistEmail_ReturnErrorResponseBadRequest409() throws Exception {


        UserDto existEmail =  new UserDto("example@gmail.com","Grisha","Privet",
                LocalDate.of(1996,5,16),null,null);
        User userExistEmail = new User(2L,"example@gmail.com","Grisha",
                LocalDate.of(1996,5,16),"Privet",null,null);

        when(userMapper.toUser(existEmail)).thenReturn(userExistEmail);

        when(userService.createUser(userMapper.toUser(existEmail)))
                .thenThrow( new UserEmailExistException(" This email example@gmail.com is busy "));


        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(existEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" User email exist "))
                .andExpect(jsonPath("$.detail").value(" This email " + userExistEmail.getEmail() + " is busy " ))
                .andExpect(jsonPath("$.path").value("/api/v1/users" ));

                verify(userService, times(1)).createUser(userExistEmail);

    }

    @Test
    void createUser_ByWrongAge_ReturnErrorResponseBedRequest400() throws Exception {

        testUser.setBirthDate(LocalDate.of(3000,1,1));

        when(userService.createUser(testUser)).thenThrow(new UserWrongDateException(" Birth date must be earlier than current date "));


        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Wrong date "))
                .andExpect(jsonPath("$.detail").value(" Birth date must be earlier than current date "))
                .andExpect(jsonPath("$.path").value("/api/v1/users" ));

        verify(userService, times(1)).createUser(testUser);
    }
    @Test
    void createUser_ByWrongDate_ReturnErrorResponseUnprocessableEntity422() throws Exception {

        testUser.setBirthDate(LocalDate.of(2015,1,1));

        when(userService.createUser(testUser)).thenThrow(new UserWrongAgeException(" User must be at least 18 years old "));


        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Wrong age "))
                .andExpect(jsonPath("$.detail").value(" User must be at least 18 years old " ))
                .andExpect(jsonPath("$.path").value("/api/v1/users" ));
        verify(userService, times(1)).createUser(testUser);
    }
    @Test
    void createUser_ByEmptyEmail_ReturnValidationErrorResponseBadRequest400()  throws Exception {

         testUserDto.setEmail(null);

        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.detail").value(" Email can`t be empty " ))
                .andExpect(jsonPath("$.path").value("/api/v1/users" ));
        verify(userService, never()).createUser(testUser);
    }

    @Test
    void updateUserEmail_Return200Ok() throws Exception {
        String updateEmail = "example123@gmail.com";
        Long id = 1L;
        testUser.setEmail(updateEmail);
        testUserDto.setEmail(updateEmail);

        when(userService.updateUserEmail(id,updateEmail)).thenReturn(testUser);

        mockMvc.perform(patch("/api/v1/users/{id}", id)
                        .content(objectMapper.writeValueAsString(emailDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.email").value(updateEmail))
                .andExpect(jsonPath("$.path").value("/api/v1/users/"+ id ));
        verify(userService, times(1)).updateUserEmail(id, updateEmail);


    }

    @Test
    void updateUserEmail_ByExistEmail_ReturnErrorResponseConflict409() throws Exception {
        String updateEmail = "example123@gmail.com";
        Long id = 1L;

        when(userService.updateUserEmail(id, updateEmail))
                .thenThrow(new UserEmailExistException(" This email example123@gmail.com is busy "));

        mockMvc.perform(patch("/api/v1/users/{id}",id)
                        .content(objectMapper.writeValueAsString(emailDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" User email exist "))
                .andExpect(jsonPath("$.detail").value(" This email " + updateEmail + " is busy " ))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));

        verify(userService, times(1)).updateUserEmail(id,updateEmail);

    }

    @Test
    void updateUserEmail_ByIllegalId_ReturnErrorResponseBadRequest400() throws Exception {
        String updateEmail = "example123@gmail.com";
        Long id = -1L;

        when(userService.updateUserEmail(id, updateEmail))
                .thenThrow(new IllegalArgumentException("User ID must be greater than 0"));

        mockMvc.perform(patch("/api/v1/users/{id}",id)
                        .content(objectMapper.writeValueAsString(emailDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.detail").value("User ID must be greater than 0"))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));

        verify(userService, times(1)).updateUserEmail(id,updateEmail);

    }
    @Test
    void updateUserEmail_ById_ReturnErrorResponseNotFound404() throws Exception {
        String updateEmail = "example123@gmail.com";
        Long id = 100L;

        when(userService.updateUserEmail(id, updateEmail))
                .thenThrow(new UserNotFoundException(" User with this " + id + " not found "));

        mockMvc.perform(patch("/api/v1/users/{id}",id)
                        .content(objectMapper.writeValueAsString(emailDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" User not found "))
                .andExpect(jsonPath("$.detail").value(" User with this " + id + " not found "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));

        verify(userService, times(1)).updateUserEmail(id,updateEmail);

    }

    @Test
    void updateUserEmail_ById_ReturnErrorResponseBadRequest400() throws Exception {

        Long id = 1L;
        EmailDto invalidEmail = new EmailDto(" 123");

        mockMvc.perform(patch("/api/v1/users/{id}",id)
                        .content(objectMapper.writeValueAsString(invalidEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.detail").value(" Input correct email address "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));

        verify(userService, never()).updateUserEmail(id,invalidEmail.getEmail());

    }


        @Test
    void updateAllFieldsUser_ReturnUserResponseOk200() throws Exception {
           Long id = 1L;
            UserDto updatedUserDto =  new UserDto("example22@gmail.com","Grisha","Privet",
                    LocalDate.of(1996,5,16),null,null);
            User updatedUser = new User(1L,"example22@gmail.com","Grisha",
                    LocalDate.of(1996,5,16),"Privet",null,null);
            when(userMapper.toUser(updatedUserDto)).thenReturn(updatedUser);
            when(userMapper.toDTO(updatedUser)).thenReturn(updatedUserDto);

            when(userService.updateUser(id,updatedUser)).thenReturn(updatedUser);

            mockMvc.perform(put("/api/v1/users/{id}", id)
                            .content(objectMapper.writeValueAsString(updatedUserDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.firstName").value("Grisha"))
                    .andExpect(jsonPath("$.data.lastName").value("Privet"))
                    .andExpect(jsonPath("$.path").value("/api/v1/users/" + id));
            verify(userService,times(1)).updateUser(id,updatedUser);

    }
    @Test
    void updateAllFieldsUser_ByIllegalId_ReturnUserErrorResponseBadRequest400() throws Exception {
        Long id = -1L;
        UserDto updatedUserDto =  new UserDto("example23@gmail.com","Grisha","Privet",
                LocalDate.of(1996,5,16),null,null);
        User updatedUser = new User(1L,"example23@gmail.com","Grisha",
                LocalDate.of(1996,5,16),"Privet",null,null);
        when(userMapper.toUser(updatedUserDto)).thenReturn(updatedUser);

        when(userService.updateUser(id,updatedUser)).thenThrow(new IllegalArgumentException("User ID must be greater than 0"));

        mockMvc.perform(put("/api/v1/users/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.detail").value("User ID must be greater than 0"))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id));
        verify(userService,times(1)).updateUser(id,updatedUser);
    }
    @Test
    void updateAllFieldsUser_ByExistEmail_ReturnUserErrorResponseConflict409() throws Exception {
        Long id = 1L;
        UserDto updatedUserDto =  new UserDto("example@gmail.com","Grisha","Privet",
                LocalDate.of(1996,5,16),null,null);
        User updatedUser = new User(1L,"example@gmail.com","Grisha",
                LocalDate.of(1996,5,16),"Privet",null,null);
        when(userMapper.toUser(updatedUserDto)).thenReturn(updatedUser);

        when(userService.updateUser(id,updatedUser))
                .thenThrow(new UserEmailExistException(" This email " + updatedUserDto.getEmail() + " is busy "));

        mockMvc.perform(put("/api/v1/users/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" User email exist "))
                .andExpect(jsonPath("$.detail").value(" This email " + updatedUserDto.getEmail() + " is busy "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id));
        verify(userService,times(1)).updateUser(id,updatedUser);
    }
    @Test
    void updateAllFieldsUser_ByInvalidEmail_ReturnUserErrorResponseBadRequest400() throws Exception {
        Long id = 1L;
        UserDto updatedUserDto =  new UserDto(" 12345","Grisha","Privet",
                LocalDate.of(1990,5,16),null,null);

        mockMvc.perform(put("/api/v1/users/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.detail").value(" Input correct email address "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id));
        verify(userService,never()).updateUser(any(Long.class),any(User.class));
    }

    @Test
    void deleteUser_ById_ReturnNoContent204() throws Exception {

        Long id = 1L;

      mockMvc.perform(delete("/api/v1/users/{id}",id)
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isNoContent());

    }
    @Test
    void deleteUser_ById_ReturnUserErrorResponseNotFound404() throws Exception {

        Long id = 5L;
        doThrow(new UserNotFoundException(" User with this " + id + " not found ")).when(userService).deleteUser(id);
        mockMvc.perform(delete("/api/v1/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(" User not found "))
                .andExpect(jsonPath("$.detail").value(" User with this " + id + " not found "))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));
        verify(userService,times(1)).deleteUser(id);

    }
    @Test
    void deleteUser_ByIllegalId_ReturnUserErrorResponseBadRequest400() throws Exception {

        Long id = -1L;
        doThrow(new IllegalArgumentException("User ID must be greater than 0")).when(userService).deleteUser(id);
        mockMvc.perform(delete("/api/v1/users/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.detail").value("User ID must be greater than 0"))
                .andExpect(jsonPath("$.path").value("/api/v1/users/" + id ));
        verify(userService,times(1)).deleteUser(id);

    }
    @Test
    void searchUsersByDateRange_ByDateRangeDto_ReturnUserResponseOk200() throws Exception {


        when(userService.searchUsersByBirthDateRange(dateRangeDto.getFromDate(), dateRangeDto.getToDate(),pageable)).thenReturn(pageUser);

        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dateRangeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.[0].email").value("example@gmail.com"))
                .andExpect(jsonPath("$.data.content.[0].firstName").value("Serhii"))
                .andExpect(jsonPath("$.data.content.[0].lastName").value("Shapoval"));
        verify(userService,times(1))
                .searchUsersByBirthDateRange(dateRangeDto.getFromDate(), dateRangeDto.getToDate(), pageable );

    }
    @Test
    void searchUsersByDateRange_ByWrongDateRangeDto_ReturnUserErrorResponseBadRequest400() throws Exception {
        DateRangeDto wrongDate = new DateRangeDto(LocalDate.of(2024,1,1),
                LocalDate.of(2023,1,1));


        when(userService.searchUsersByBirthDateRange(wrongDate.getFromDate(), wrongDate.getToDate(), pageable ))
                .thenThrow((new UserWrongDateException(" From date "  + wrongDate.getFromDate().toString() + " must be before "
                        + wrongDate.getToDate().toString())));

        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongDate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(" Wrong date "))
                .andExpect(jsonPath("$.detail").
                        value(" From date "  + wrongDate.getFromDate().toString() + " must be before " + wrongDate.getToDate().toString()))
                .andExpect(jsonPath("$.path").value("/api/v1/users"));
        verify(userService,times(1))
                .searchUsersByBirthDateRange(wrongDate.getFromDate(),wrongDate.getToDate(), pageable );

    }

    @Test
    void searchUsersByDateRange_ByInvalidDateRangeDto_ReturnUserErrorResponseBadRequest400() throws Exception {

        dateRangeDto.setFromDate(null);

        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dateRangeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(" Validation error "))
                .andExpect(jsonPath("$.detail").
                        value(" From date can`t be null or empty. This date is required "))
                .andExpect(jsonPath("$.path").value("/api/v1/users"));
        verify(userService,never())
                .searchUsersByBirthDateRange(dateRangeDto.getFromDate(),dateRangeDto.getToDate(), pageable );

    }
}