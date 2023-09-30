package com.shapoval.clearsolution.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserResponse <T>{

    private T data;

    private String path;

}
