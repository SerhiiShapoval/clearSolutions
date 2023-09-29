package com.shapoval.clearsolution.repository;

import com.shapoval.clearsolution.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(String email);

    List<User> findUsersByBirthDateBetween(LocalDate from, LocalDate to);


}
