package com.shapoval.clearsolution.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserNotFoundException extends RuntimeException {

    private String message;

}
