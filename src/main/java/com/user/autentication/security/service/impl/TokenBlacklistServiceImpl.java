package com.user.autentication.security.service.impl;

import com.user.autentication.security.service.TokenBlackListService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistServiceImpl implements TokenBlackListService {
    private Set<String> blackListToken = new HashSet<>();

    @Override
    public void addTokenToBlackList(String token) {
        blackListToken.add(token);
    }

    @Override
    public boolean isTokenBlackList(String token) {
        return blackListToken.contains(token);
    }
}
