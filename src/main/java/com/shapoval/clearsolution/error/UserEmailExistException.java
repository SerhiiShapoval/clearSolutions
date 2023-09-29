package com.shapoval.clearsolution.error;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class UserEmailExistException extends RuntimeException{


    private String message;


}
