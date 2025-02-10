package com.example.bookhubbackend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import java.util.Date;

public class JwtTokenUtil {

    private static final long EXPIRATION_TIME = 86400000; // 1일
    private static final String SECRET_KEY = "MySuperSecretKeyForJWTMySuperSecretKeyForJWT"; // 환경 변수 사용 권장
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // SecretKey 객체로 변환


    // JWT 토큰 생성
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 사용자 정보 저장
                .setIssuedAt(new Date()) // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(KEY, SignatureAlgorithm.HS256) // SecretKey 객체 사용
                .compact();
    }

    // JWT 토큰 파싱 (Claims 반환)
    public static Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY) // SecretKey 객체 사용
                    .build()
                    .parseClaimsJws(token) // 토큰을 파싱하여 JWT Claims 정보 추출.
                    .getBody(); // Claims 반환
        } catch (JwtException e) {
            return null; // 유효하지 않은 토큰일 경우 null 반환
        }
    }
}
