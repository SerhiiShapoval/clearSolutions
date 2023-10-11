package com.shapoval.clearsolution.service;


import com.shapoval.clearsolution.error.UserEmailExistException;
import com.shapoval.clearsolution.error.UserNotFoundException;
import com.shapoval.clearsolution.error.UserWrongAgeException;
import com.shapoval.clearsolution.error.UserWrongDateException;
import com.shapoval.clearsolution.model.Address;
import com.shapoval.clearsolution.model.User;
import com.shapoval.clearsolution.repository.UserRepository;
import com.shapoval.clearsolution.service.serviceImpl.UserServiceImpl;
import com.shapoval.clearsolution.web.dto.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User userValid;
    private User userInvalid;

    private User userUpdate;
    private Pageable pageable;
    private List<User> userList;

    private Page<User> pageList;


    @BeforeEach
    public void setUp() {

        List<String> phones = List.of("123-456-789");

        Address address =
                new Address("Ukraine", "Kyiv", "Street", "50", "50", "1234");

        this.userValid = new User(1L, "example@gmail.com", "Serhii",
                LocalDate.of(1991, 1, 13), "Shapoval", address, phones);

        this.userInvalid = new User(2L, "example1@gmail.com", "Serhii",
                LocalDate.of(2010, 1, 13), "Vashkulat", null, null);

        this.userUpdate = new User(1L, "update@email.com", "Serhii",
                LocalDate.of(1991, 1, 13), "Shapoval", address, phones);
        userList= new ArrayList<>();
        userList.add(userValid);
        pageable = PageRequest.of(0,10);
        pageList = new PageImpl<>(userList,pageable,userList.size());
    }

    @Test
    void testCreateUser_ByValidValues_ReturnSavedUser() {



        when(userRepository.save(userValid)).thenReturn(userValid);

        User userResult = userService.createUser(userValid);


        verify(userRepository, times(1)).save(userValid);

        assertNotNull(userResult);
        assertEquals(userResult.getFirstName(), userValid.getFirstName());
        assertEquals(userResult.getLastName(), userValid.getLastName());

    }

    @Test
    void testCreateUser_ByEmailExist_ReturnUserEmailExistException() {

        String emailExist = "example1@gmail.com";

       when(userRepository.existsUserByEmail(emailExist)).thenThrow(UserEmailExistException.class);

        assertEquals(emailExist, userInvalid.getEmail());
        assertThrows(UserEmailExistException.class, () -> userService.createUser(userInvalid));



    }

    @Test
    void testCreateUser_ByWrongAge_ReturnUserWrongAgeException() {

        int currentAge = Period.between(userInvalid.getBirthDate(), LocalDate.now()).getYears();
        int age = 18;


        when(userService.createUser(userInvalid)).thenThrow(UserWrongAgeException.class);
        assertThrows(UserWrongAgeException.class, () -> userService.createUser(userInvalid));

        assertTrue(age > currentAge);
        verify(userRepository,never()).save(userValid);
    }

    @Test
    void testCreateUser_ByWrongBirthDate_ReturnUserWrongDateException() {

        User wrongDate = User.builder()
                .birthDate(LocalDate.of(3000, 1, 1))
                .build();

        assertThrows(UserWrongDateException.class, () -> userService.createUser(wrongDate));
        assertTrue(wrongDate.getBirthDate().isAfter(LocalDate.now()));

    }

    @Test
    void testDeleteUser() {

        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));

        User willBeDeleted = userRepository.findById(id).orElseThrow();

        assertEquals(id,willBeDeleted.getId());
        assertEquals(willBeDeleted.getFirstName(),userValid.getFirstName());
        assertEquals(willBeDeleted.getEmail(),userValid.getEmail());

       doNothing().when(userRepository).delete(userValid);

        verify(userRepository,times(1)).findById(id);

        userService.deleteUser(id);

       verify(userRepository,times(1)).delete(userValid);

    }

    @Test
    void testFindUserById_ValidId_ReturnUser() {

        Long id = 1l;

       when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));

       User currentUserBiId = userService.findUserById(id);

       assertNotNull(currentUserBiId);
       assertEquals(currentUserBiId.getEmail(),userValid.getEmail());
       assertEquals(currentUserBiId.getFirstName(),userValid.getFirstName());
       assertEquals(id, currentUserBiId.getId());

       verify(userRepository,times(1)).findById(id);

    }

    @Test
    void testFindUserById_InvalidId_ReturnUserNotFoundExceptionUser() {

        Long id = 3l;

        when(userRepository.findById(id)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, ()->userService.findUserById(id));

    }


    @Test
    void testUpdateUserEmail_ReturnUpdateUser() {

        Long id = 1l;
        String emailUpdate = "update@email.com";

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));

        when(userRepository.save(userValid)).thenReturn(userUpdate);


        User result = userService.updateUserEmail(id, emailUpdate);

        assertNotNull(result);
        assertEquals(userUpdate, result);
        assertEquals(emailUpdate, result.getEmail());
        assertEquals(id, userUpdate.getId());
        verify(userRepository,times(1)).findById(id);
        verify(userRepository,times(1)).save(userValid);

    }


    @Test
    void testUpdateUserAllFields_ReturnUpdatedUser() {

        Long id = 1L;
        User updatedUserFields =
                new User(id,"koko@ldal.com","Grisha"
                        ,LocalDate.of(1997,1,1),"Grisha",null,null);

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));
        when(userRepository.save(updatedUserFields)).thenReturn(updatedUserFields);

        User userFromDB = userRepository.findById(id).orElseThrow();

        assertEquals(updatedUserFields.getId(),userFromDB.getId());
        assertNotEquals(userFromDB.getEmail(),updatedUserFields.getEmail());
        assertNotEquals(userFromDB.getFirstName(),updatedUserFields.getFirstName());
        assertNotEquals(updatedUserFields, userFromDB);
        verify(userRepository,times(1)).findById(id);

        User result = userService.updateUser(id,updatedUserFields);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(updatedUserFields, result);
        assertEquals(updatedUserFields.getFirstName(),result.getFirstName());
        assertEquals(updatedUserFields.getEmail(),result.getEmail());
        verify(userRepository,times(1)).save(updatedUserFields);

    }

    @Test
    void testSearchUsersByBirthDateRange_ValidDate_ReturnListUsers() {


        LocalDate from = LocalDate.of(1900,1,1);
        LocalDate to = LocalDate.of(2000,1,1);

        when(userRepository.findUsersByBirthDateBetween(from,to,pageable)).thenReturn(pageList);

        var result = userService.searchUsersByBirthDateRange(from,to,pageable );

        assertTrue(result.getTotalElements() > 0);
        assertTrue(result.getContent().get(0).getBirthDate().isBefore(to));
        assertFalse(result.getContent().get(0).getBirthDate().isBefore(from));
        assertEquals(result.getContent().get(0).getEmail(), pageList.getContent().get(0).getEmail());
        assertEquals(result.getContent().get(0).getFirstName(),pageList.getContent().get(0).getFirstName());
        assertEquals(result.getPageable(), pageable);
        assertEquals(result.getTotalElements(), pageList.getTotalElements() );
        verify(userRepository, times(1)).findUsersByBirthDateBetween(from,to,pageable);

    }

    @Test
    void testSearchUsersByBirthDateRange_WrongDate_ReturnUserWrongDateException() {


        LocalDate from = LocalDate.of(3000,1,1);
        LocalDate to = LocalDate.of(2000,1,1);

       assertThrows(UserWrongDateException.class, ()-> userService.searchUsersByBirthDateRange(from,to, pageable));
        assertTrue(from.isAfter(to));
        verify(userRepository,never()).findUsersByBirthDateBetween(from,to,pageable);

    }
}
