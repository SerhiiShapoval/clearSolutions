package com.shapoval.clearsolution.error;



import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
@Builder
public class UserErrorResponse {

   private String error;
   private String detail;
   private String path;
   private LocalDateTime timestamp;

}
