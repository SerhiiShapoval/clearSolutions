package com.shapoval.clearsolution.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorTitle {
    USER_NOT_FOUND(" User not found "),
    WRONG_DATE(" Wrong date "),
    WRONG_AGE(" Wrong age "),
    USER_EMAIL_EXIST(" User email exist "),
    USER_INCORRECT_DATA(" User incorrect data ");

    private final String error;





}
