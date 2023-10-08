package com.shapoval.clearsolution.service;

import com.shapoval.clearsolution.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserService {

    User createUser(User user);

    void deleteUser(Long id);

    User findUserById(Long id);

    User updateUserEmail(Long id, String email);

    User updateUser(Long id, User user);

    Page<User> searchUsersByBirthDateRange(LocalDate from, LocalDate to,Pageable pageable);
}
