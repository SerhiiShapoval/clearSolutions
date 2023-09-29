package com.shapoval.clearsolution.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@Data
@Builder
public class UserResponse <T>{

    private T data;

    private String path;

}
