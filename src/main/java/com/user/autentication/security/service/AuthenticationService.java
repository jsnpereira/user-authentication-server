package com.user.autentication.security.service;

import com.user.autentication.security.dto.request.SignUpRequest;
import com.user.autentication.security.dto.request.SigninRequest;
import com.user.autentication.security.dto.response.JwtAuthenticationResponse;
import com.user.autentication.security.exception.UsernameHaveBeenCreatedException;

public interface AuthenticationService {
    void signup(SignUpRequest request) throws UsernameHaveBeenCreatedException;

    JwtAuthenticationResponse signin(SigninRequest request);
}
