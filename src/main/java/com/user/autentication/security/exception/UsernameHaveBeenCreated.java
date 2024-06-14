package com.user.autentication.security.exception;

public class UsernameHaveBeenCreated extends RuntimeException {
    public UsernameHaveBeenCreated(String username) {
        super(String.format("Username[%s] has been created in database",username));
    }
}
