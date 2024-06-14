package com.user.autentication.security.exception;

public class EmailHaveBeenCreated extends RuntimeException {
    public EmailHaveBeenCreated(String username) {
        super(String.format("Email[%s] has been created in database",username));
    }
}
