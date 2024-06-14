package com.user.autentication.security.handler.message;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorValidMessageDTO {
    private String field;
    private String message;
}
