package com.shapoval.clearsolution.web.dto;


import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateRangeDto {


   @NotNull(message = " From date can`t be null or empty. This date is required ")
   private LocalDate fromDate;

   @NotNull(message = "To date can`t be null or empty. This date is required ")
   private LocalDate toDate;
}
