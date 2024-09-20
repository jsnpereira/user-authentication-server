package com.user.autentication.security.service;

public interface TokenBlackListService {
    public void addTokenToBlackList(String token);
    public boolean isTokenBlackList(String token);
}
