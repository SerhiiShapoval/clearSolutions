package com.shapoval.clearsolution.error;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class UserWrongAgeException extends RuntimeException {

    private String message;


}
