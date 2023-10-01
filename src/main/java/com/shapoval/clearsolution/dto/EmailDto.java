package com.shapoval.clearsolution.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class EmailDto {
    @NotBlank(message = " Email can`t be empty " )
    @NotNull(message = " Email can`t be null ")
    @Pattern(regexp ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$",
            message = " Input correct email address ")
    private String email;

    public EmailDto() {

    }
}
