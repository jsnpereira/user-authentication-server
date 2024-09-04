package com.user.autentication.security.exception;

public class UsernameHaveBeenCreatedException extends RuntimeException {
    public UsernameHaveBeenCreatedException(String username) {
        super(String.format("%s has been created in database",username));
    }
}
