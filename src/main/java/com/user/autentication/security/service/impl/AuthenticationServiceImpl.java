package com.user.autentication.security.service.impl;

import com.user.autentication.security.entities.Role;
import com.user.autentication.security.entities.User;
import com.user.autentication.security.exception.EmailHaveBeenCreated;
import com.user.autentication.security.exception.InvalidCredentialsException;
import com.user.autentication.security.exception.UsernameHaveBeenCreatedException;
import com.user.autentication.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.autentication.security.dto.request.SignUpRequest;
import com.user.autentication.security.dto.request.SigninRequest;
import com.user.autentication.security.dto.response.JwtAuthenticationResponse;
import com.user.autentication.security.service.AuthenticationService;
import com.user.autentication.security.service.JwtService;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public void signup(SignUpRequest request) throws UsernameHaveBeenCreatedException {

        if(userRepository.existsByUsername(request.getUsername())){
            throw new UsernameHaveBeenCreatedException(request.getUsername());
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailHaveBeenCreated(request.getEmail());
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER.USER).build();
        userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateToken((UserDetails) authentication.getPrincipal());
            return JwtAuthenticationResponse.builder().token(jwt).type("Bearer").build();
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Usuário ou senha inválidos.");
        }
    }
}
