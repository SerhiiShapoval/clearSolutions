package com.shapoval.clearsolution.service;


import com.shapoval.clearsolution.error.UserEmailExistException;
import com.shapoval.clearsolution.error.UserNotFoundException;
import com.shapoval.clearsolution.error.UserWrongAgeException;
import com.shapoval.clearsolution.error.UserWrongDateException;
import com.shapoval.clearsolution.model.Address;
import com.shapoval.clearsolution.model.User;
import com.shapoval.clearsolution.repository.UserRepository;
import com.shapoval.clearsolution.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;


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

    private List<User> userList;

    @BeforeEach
    public void setUp() {

        userList = new ArrayList<>();

        List<String> phones = List.of("123-456-789");

        Address address =
                new Address("Ukraine", "Kyiv", "Street", "50", "50", "1234");

        this.userValid = new User(1L, "example@gmail.com", "Serhii",
                LocalDate.of(1991, 1, 13), "Shapoval", address, phones);

        this.userInvalid = new User(2L, "example1@gmail.com", "Serhii",
                LocalDate.of(2010, 1, 13), "Vashkulat", null, null);

        this.userUpdate = new User(1L, "update@email.com", "Serhii",
                LocalDate.of(1991, 1, 13), "Shapoval", address, phones);
    }

    @Test
    void testCreateUser_ByValidValues_ReturnSavedUser() {

        int countUser = userList.size();

        when(userRepository.save(userValid)).thenReturn(userValid);

        User userResult = userService.createUser(userValid);
        userList.add(userResult);

        verify(userRepository, times(1)).save(userValid);

        assertNotNull(userResult);
        assertEquals(userResult.getFirstName(), userValid.getFirstName());
        assertEquals(userResult.getLastName(), userValid.getLastName());
        assertTrue(userList.contains(userResult));
        assertNotEquals(countUser, userList.size());

    }

    @Test
    void testCreateUser_ByEmailExist_ReturnUserEmailExistException() {

        int countUser = userList.size();
        String emailExist = "example1@gmail.com";

       when(userRepository.existsUserByEmail(emailExist)).thenThrow(UserEmailExistException.class);

        assertEquals(emailExist, userInvalid.getEmail());
        assertThrows(UserEmailExistException.class, () -> userService.createUser(userInvalid));
        assertFalse(userList.contains(userInvalid));
        assertEquals(countUser, userList.size());

    }

    @Test
    void TestCreateUser_ByWrongAge_ReturnUserWrongAgeException() {

        int countUser = userList.size();
        int currentAge = Period.between(userInvalid.getBirthDate(), LocalDate.now()).getYears();
        int age = 18;

        when(userService.createUser(userInvalid)).thenThrow(UserWrongAgeException.class);

        assertThrows(UserWrongAgeException.class, () -> userService.createUser(userInvalid));
        assertFalse(userList.contains(userInvalid));
        assertEquals(countUser, userList.size());
        assertTrue(age > currentAge);

    }

    @Test
    void TestCreateUser_ByWrongBirthDate_ReturnUserWrongDateException() {

        User wrongDate = User.builder()
                .birthDate(LocalDate.of(3000, 1, 1))
                .build();

        int countUser = userList.size();

        assertThrows(UserWrongDateException.class, () -> userService.createUser(wrongDate));
        assertFalse(userList.contains(userInvalid));
        assertEquals(countUser, userList.size());
        assertTrue(wrongDate.getBirthDate().isAfter(LocalDate.now()));

    }

    @Test
    void testDeleteUser() {

        Long id = 1L;
        userList.add(userValid);
        int countUser = userList.size();

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));
        doNothing().when(userRepository).delete(any(User.class));

        User willBeDeleted = userRepository.findById(id).orElseThrow();

        assertEquals(id,willBeDeleted.getId());
        verify(userRepository,times(1)).findById(id);

        userService.deleteUser(id);
        boolean answer = userList.remove(userValid);

        assertTrue(answer);
        assertNotEquals(countUser, userList.size());
        assertFalse(userList.contains(userValid));
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void testFindUserById_ValidId_ReturnUser() {

        Long id = 1l;

       when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));

       User currentUser = userService.findUserById(id);

       verify(userRepository,times(1)).findById(id);

       assertNotNull(currentUser);
       assertEquals(currentUser,userValid);
       assertEquals(id, currentUser.getId());

    }

    @Test
    void testFindUserById_InvalidId_ReturnUserNotFoundExceptionUser() {

        Long id = 3l;
        User notFound = User.builder()
                .id(id)
                .build();

        assertFalse(userList.contains(notFound));
        assertThrows(UserNotFoundException.class, ()->userService.findUserById(id));

    }

    @Test
    void testFindUserById_InvalidId_ReturnIllegalArgumentException() {

        Long id = -1L;

        assertThrows(IllegalArgumentException.class, ()->userService.findUserById(id));

        verify(userRepository,never()).findById(id);

    }

    @Test
    void testUpdateUserEmail_ReturnUpdateUser() {

        Long id = 1l;
        String emailUpdate = "update@email.com";

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));
        when(userRepository.save(any(User.class))).thenReturn(userUpdate);


        User result = userService.updateUserEmail(id, emailUpdate);

        assertNotNull(result);
        assertEquals(userUpdate, result);
        assertEquals(emailUpdate, result.getEmail());
        assertEquals(id, userUpdate.getId());
        verify(userRepository,times(1)).findById(id);
        verify(userRepository,times(1)).save(any(User.class));

    }


    @Test
    void testUpdateUserAllFields_ReturnUpdateUser() {

        Long id = 1L;
        User updatedUserFields =
                new User(id,"koko@ldal.com","Grisha"
                        ,LocalDate.of(1997,1,1),"Grisha",null,null);

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(userValid));
        when(userRepository.save(updatedUserFields)).thenReturn(updatedUserFields);

        User userFromDB = userRepository.findById(id).orElseThrow();

        assertEquals(updatedUserFields.getId(),userFromDB.getId());
        assertNotEquals(updatedUserFields, userFromDB);
        verify(userRepository,times(1)).findById(id);

        User result = userService.updateUser(id,updatedUserFields);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(updatedUserFields, result);
        verify(userRepository,times(1)).save(updatedUserFields);

    }

    @Test
    void testSearchUsersByBirthDateRange_ValidDate_ReturnListUsers() {

        userList.add(userValid);

        LocalDate from = LocalDate.of(1900,1,1);
        LocalDate to = LocalDate.of(2000,1,1);

        when(userRepository.findUsersByBirthDateBetween(from,to)).thenReturn(userList);

        var result = userService.searchUsersByBirthDateRange(from,to);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0).getBirthDate().isBefore(to));
        assertFalse(result.get(0).getBirthDate().isBefore(from));
    }

    @Test
    void testSearchUsersByBirthDateRange_WrongDate_ReturnUserWrongDateException() {


        LocalDate from = LocalDate.of(3000,1,1);
        LocalDate to = LocalDate.of(2000,1,1);

        assertThrows(UserWrongDateException.class, ()-> userService.searchUsersByBirthDateRange(from,to));
        assertTrue(from.isAfter(to));
        verify(userRepository,never()).findUsersByBirthDateBetween(from,to);

    }
}
