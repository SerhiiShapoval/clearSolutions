package com.shapoval.clearsolution.dto;


import com.shapoval.clearsolution.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDto {

    @NotBlank(message = " Email can`t be empty " )
    @Pattern(regexp ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$",
            message = " Input correct email address for registration ")
    private String email;

    @NotBlank(message = " First name can`t be empty " )
    private String firstName;

    @NotBlank(message = " Last name can`t be empty " )
    private String lastName;

    @NotNull(message = " Birth day can`t be empty " )
    private LocalDate birthDate;

    private Address address;

    private List<String> phoneList;

}
