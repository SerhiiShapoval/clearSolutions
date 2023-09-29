package com.shapoval.clearsolution.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class UserWrongDateException extends RuntimeException{

    private String message;
}
