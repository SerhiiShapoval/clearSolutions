package com.shapoval.clearsolution.service.serviceImpl;

import com.shapoval.clearsolution.error.UserEmailExistException;
import com.shapoval.clearsolution.error.UserNotFoundException;
import com.shapoval.clearsolution.error.UserWrongAgeException;
import com.shapoval.clearsolution.error.UserWrongDateException;
import com.shapoval.clearsolution.model.User;
import com.shapoval.clearsolution.repository.UserRepository;
import com.shapoval.clearsolution.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Value("${user.minAge}")
    private  int minAgeUser;


    @Override
    public Page<User> searchUsersByBirthDateRange(LocalDate from, LocalDate to, Pageable pageable) {

        validateDateRange(from,to);
        log.info(" Search users by birth date from {} to {}", from, to);
        return userRepository.findUsersByBirthDateBetween(from,to,pageable);
    }

    @Override
    public User updateUser(Long id, User user) {

        validationUserEmail(user.getEmail());

        User userUpdate = findUserById(id);
        userUpdate.setEmail(user.getEmail());
        userUpdate.setFirstName(user.getFirstName());
        userUpdate.setLastName(user.getLastName());
        userUpdate.setBirthDate(user.getBirthDate());
        userUpdate.setAddress(user.getAddress());
        userUpdate.setPhoneNumbers(user.getPhoneNumbers());

        log.info(" Update all user fields ");
        return userRepository.save(userUpdate);

    }

    @Override
    @Transactional
    public User updateUserEmail(Long id, String email) {

        validationUserEmail(email);

        User user = findUserById(id);
        user.setEmail(email);

        log.info(" Update user email {} ", email);
       return userRepository.save(user);
    }

    @Override
    public User createUser(User user) {

        validationDate(user.getBirthDate());

        validationUserAge(user);

        validationUserEmail(user.getEmail());

        log.info(" Save to database user: {} ", user);
        return userRepository.save(user);

    }

    @Override
    public User findUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->{
                    log.error(" User whit id: {} not found ", id);
                    return new UserNotFoundException(" User with this " + id + " not found ");
                });
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        log.info(" Delete user with id: {}", id);
        userRepository.delete(findUserById(id));

    }

    private void validationDate(LocalDate birthDate){
        if (birthDate.isAfter(LocalDate.now())){
            log.error(" The date of birth is incorrect : {} ",birthDate );
            throw new UserWrongDateException(" Birth date must be earlier than current date ");
        }
    }

    private void validationUserAge(User user){
        if (minAgeUser >= Period.between(user.getBirthDate(), LocalDate.now()).getYears()){
            log.error(" The user {} {} is under 18 years old . ", user.getFirstName(), user.getLastName());
            throw new UserWrongAgeException(" User must be at least 18 years old ");
        }
    }

    private void validationUserEmail(String email){

        if (userRepository.existsUserByEmail(email)){
            log.error(" Email is busy {}", email);
            throw new UserEmailExistException(" This email " + email + " is busy " );
        }
    }

    private void validateDateRange(LocalDate from, LocalDate to){
        if (from.isAfter(to)){
            log.error(" Wrong date range. ");
            throw new UserWrongDateException(" From date " + from + " must be before To date " + to);
        }

    }
}
