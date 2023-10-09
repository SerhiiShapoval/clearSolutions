package com.shapoval.clearsolution.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;


@Data
@Builder

public class UserResponse <T>{

    private T data;

    private String path;

}
