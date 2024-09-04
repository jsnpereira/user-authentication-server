package com.user.autentication.security.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotNull(message = "The username field is mandatory")
    @Size(min = 3, message = "The field mustn't be empty.")
    private String username;
    @NotNull(message = "The email field is mandatory")
    private String email;
    @NotNull(message = "The password field is mandatory")
    private String password;
}
