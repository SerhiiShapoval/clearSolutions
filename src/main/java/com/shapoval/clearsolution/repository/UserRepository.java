package com.shapoval.clearsolution.repository;

import com.shapoval.clearsolution.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(String email);
   @Query(value = "SELECT u from User u left join fetch u.phoneNumbers  where u.birthDate between :from and :to",
   countQuery = "select count (u) from User u")
    Page<User> findUsersByBirthDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);


}
