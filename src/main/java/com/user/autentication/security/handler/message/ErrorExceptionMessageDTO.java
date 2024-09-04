package com.user.autentication.security.handler.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties()
public class ErrorExceptionMessageDTO {
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime dateTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ErrorValidMessageDTO> errorList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorValidMessageDTO error;
}
