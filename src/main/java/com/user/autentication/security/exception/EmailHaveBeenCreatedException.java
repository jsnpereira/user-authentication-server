package com.user.autentication.security.exception;

public class EmailHaveBeenCreatedException extends RuntimeException {
    public EmailHaveBeenCreatedException(String username) {
        super(String.format("%s has been created in database",username));
    }
}
