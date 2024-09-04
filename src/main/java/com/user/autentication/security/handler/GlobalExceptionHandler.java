package com.user.autentication.security.handler;

import com.user.autentication.security.exception.EmailHaveBeenCreated;
import com.user.autentication.security.exception.UsernameHaveBeenCreatedException;
import com.user.autentication.security.handler.message.ErrorExceptionMessageDTO;
import com.user.autentication.security.handler.message.ErrorValidMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameHaveBeenCreatedException.class)
    public ResponseEntity<ErrorExceptionMessageDTO> handleUsernameAlreadyExistsException(UsernameHaveBeenCreatedException ex) {
        ErrorExceptionMessageDTO errorExceptionMessageDTO = new ErrorExceptionMessageDTO();
        errorExceptionMessageDTO.setDateTime(LocalDateTime.now());
        errorExceptionMessageDTO.setError(new ErrorValidMessageDTO("username", ex.getMessage()));
        return new ResponseEntity<>(errorExceptionMessageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailHaveBeenCreated.class)
    public ResponseEntity<ErrorExceptionMessageDTO> handleEmailAlreadyExistsException(EmailHaveBeenCreated ex) {
        ErrorExceptionMessageDTO errorExceptionMessageDTO = new ErrorExceptionMessageDTO();
        errorExceptionMessageDTO.setDateTime(LocalDateTime.now());
        List<ErrorValidMessageDTO> errors = new ArrayList<>();
        errors.add(new ErrorValidMessageDTO("email", ex.getMessage()));
        errorExceptionMessageDTO.setErrorList(errors);
        return new ResponseEntity<>(errorExceptionMessageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorExceptionMessageDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorExceptionMessageDTO errorExceptionMessageDTO = new ErrorExceptionMessageDTO();
        errorExceptionMessageDTO.setDateTime(LocalDateTime.now());
        List<ErrorValidMessageDTO> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach( error -> {
            errors.add(new ErrorValidMessageDTO(error.getField(), error.getDefaultMessage()));
        });

        errorExceptionMessageDTO.setErrorList(errors);
        return new ResponseEntity<>(errorExceptionMessageDTO, HttpStatus.BAD_REQUEST);
    }
}
