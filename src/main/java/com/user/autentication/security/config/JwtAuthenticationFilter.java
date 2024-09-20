package com.user.autentication.security.config;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.autentication.security.exception.InvalidCredentialsException;
import com.user.autentication.security.exception.InvalidTokenException;
import com.user.autentication.security.exception.handler.message.ErrorExceptionMessageDTO;
import com.user.autentication.security.service.JwtService;
import com.user.autentication.security.service.TokenBlackListService;
import com.user.autentication.security.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserService userService;

    @Autowired
    private TokenBlackListService tokenBlacklistService;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException, InvalidTokenException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUserName(jwt);

            if (tokenBlacklistService.isTokenBlackList(jwt)) {
                throw new InvalidCredentialsException("Unauthorized access, please make login again!");
            }

            if (StringUtils.isNotEmpty(userEmail)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userService.userDetailsService()
                        .loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
        } catch (InvalidCredentialsException ex){
           if(!response.isCommitted()){
              handleException(response, ex);
           }
        }
        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, RuntimeException ex)  throws IOException {
        // Handle InvalidCredentialsException by writing a custom response
        response.reset();
        ErrorExceptionMessageDTO errorExceptionMessageDTO = new ErrorExceptionMessageDTO();
        errorExceptionMessageDTO.setDateTime(LocalDateTime.now());
        errorExceptionMessageDTO.setMessage(ex.getMessage());
        // Write the response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorExceptionMessageDTO));
        response.flushBuffer();
    }
}
