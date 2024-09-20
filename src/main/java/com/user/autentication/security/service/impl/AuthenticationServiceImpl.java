package com.user.autentication.security.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import com.user.autentication.security.dto.response.StatusTokenResponse;
import com.user.autentication.security.entities.Role;
import com.user.autentication.security.entities.User;
import com.user.autentication.security.exception.*;
import com.user.autentication.security.repository.UserRepository;
import com.user.autentication.security.service.UserService;
import org.apache.commons.lang3.StringUtils;
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

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private final UserService userService;


    @Override
    public void signup(SignUpRequest request) throws UsernameHaveBeenCreatedException {

        if(userRepository.existsByUsername(request.getUsername())){
            throw new UsernameHaveBeenCreatedException(request.getUsername());
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailHaveBeenCreatedException(request.getEmail());
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

    @Override
    public StatusTokenResponse validToken(String token) {
        try {
            final String user;
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " para obter o token real
            } else {
                throw new InvalidTokenException("Token inválido ou mal formado");
            }

            user = jwtService.extractUserName(token);
            if (StringUtils.isNotEmpty(user)) {
                UserDetails userDetails = userService.userDetailsService()
                        .loadUserByUsername(user);
                if (jwtService.isTokenValid(token, userDetails)) {
                    return new StatusTokenResponse("Token válido. Usuário: " + user);
                } else {
                    throw new ExpiredTokenException("Token inválido ou expirado.");
                }
            } else {
                throw new InvalidTokenException("Token inválido ou mal formado");
            }
        } catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException("Token expirado");
        } catch (SignatureException ex) {
            throw new InvalidTokenException("Assinatura do token inválida");
        } catch (MalformedJwtException ex) {
            throw new InvalidTokenException("Token mal formatado");
        }
    }
}
