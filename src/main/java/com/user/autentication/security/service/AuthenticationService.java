package com.user.autentication.security.service;

import com.user.autentication.security.dto.request.SignUpRequest;
import com.user.autentication.security.dto.request.SigninRequest;
import com.user.autentication.security.dto.response.JwtAuthenticationResponse;
import com.user.autentication.security.exception.UsernameHaveBeenCreated;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request) throws UsernameHaveBeenCreated;

    JwtAuthenticationResponse signin(SigninRequest request);
}
