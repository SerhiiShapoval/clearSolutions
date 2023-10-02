package com.shapoval.clearsolution.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;


@Data
@Builder
public class UserResponse <T>{

    private T data;

    private String path;

}
