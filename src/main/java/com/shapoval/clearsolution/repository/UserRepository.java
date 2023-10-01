package com.shapoval.clearsolution.repository;

import com.shapoval.clearsolution.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(String email);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.phoneNumbers WHERE u.birthDate BETWEEN ?1 AND ?2")
    List<User> findUsersByBirthDateBetween(LocalDate from, LocalDate to);


}
