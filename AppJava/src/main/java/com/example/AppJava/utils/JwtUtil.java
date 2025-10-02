package com.example.AppJava.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtil {

    private static final Key secretKey = Keys.hmacShaKeyFor("minhaChaveSuperSecretaDeNoMinimo32Chars".getBytes());

    public static String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(secretKey)
                .compact();
    }
}
