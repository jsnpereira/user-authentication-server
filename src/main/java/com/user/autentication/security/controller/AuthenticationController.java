package com.user.autentication.security.controller;

import com.user.autentication.security.exception.UsernameHaveBeenCreatedException;
import com.user.autentication.security.service.JwtService;
import com.user.autentication.security.service.TokenBlackListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.user.autentication.security.dto.request.SignUpRequest;
import com.user.autentication.security.dto.request.SigninRequest;
import com.user.autentication.security.dto.response.JwtAuthenticationResponse;
import com.user.autentication.security.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private final AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenBlackListService tokenBlacklistService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignUpRequest request) throws UsernameHaveBeenCreatedException {
        authenticationService.signup(request);
        return ResponseEntity.ok("The "+request.getUsername()+" user was created!!!!!!!");
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(authenticationService.validToken(token));
    }

    @GetMapping("/signout")
    public ResponseEntity<?> signOut(@RequestHeader("Authorization") String token) {
        // Verifica se o token começa com "Bearer "
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " para obter o token real
        } else {
            return new ResponseEntity<>("Token bad format", HttpStatus.BAD_REQUEST);
        }

        tokenBlacklistService.addTokenToBlackList(token);
        return new ResponseEntity<>("Signout sucessfully. revoke the token.", HttpStatus.OK);
    }
}
