package com.user.autentication.security.service;

import com.user.autentication.security.dao.request.SignUpRequest;
import com.user.autentication.security.dao.request.SigninRequest;
import com.user.autentication.security.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
