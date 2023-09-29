package com.shapoval.clearsolution.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Validated
public class DateRangeDto {

   @NotNull(message = " From date can`t be null. This date is required ")
   private LocalDate fromDate;

   @NotNull(message = "To date can`t be null. This date is required ")
   private LocalDate toDate;
}
