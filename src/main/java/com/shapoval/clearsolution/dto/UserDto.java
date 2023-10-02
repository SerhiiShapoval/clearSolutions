package com.shapoval.clearsolution.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shapoval.clearsolution.model.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

    @NotBlank(message = " Email can`t be empty " )
    @Pattern(regexp ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$",
            message = " Input correct email address ")
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
