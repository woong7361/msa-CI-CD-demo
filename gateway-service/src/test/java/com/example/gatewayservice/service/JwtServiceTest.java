package com.example.gatewayservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import javax.crypto.SecretKey;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


class JwtServiceTest {

    private Environment env = Mockito.mock(Environment.class);
    private JwtService jwtService = new JwtService(env);


    @Nested
    @DisplayName("JWT 검증")
    public class verifyJWT {
        @DisplayName("정상 JWT")
        @Test
        public void success() throws Exception {
            //given
            String key = "test_secret_123456789123456789123456789123456789";
            Mockito.when(env.getProperty("token.secret"))
                    .thenReturn(key);

            String jwt = createJwt(key, "sub", "1000");

            //when
            Boolean result = jwtService.verify(jwt, "token.secret");

            //then
            assertThat(result).isTrue();
        }

        @DisplayName("만료일이 지났을때")
        @Test
        public void expiredJwt() throws Exception{
            //given
            String key = "test_secret_123456789123456789123456789123456789";
            Mockito.when(env.getProperty("token.secret"))
                    .thenReturn(key);

            String jwt = createJwt(key, "sub", "-10000");

            //when
            Boolean result = jwtService.verify(jwt, "token.secret");

            //then
            assertThat(result).isFalse();
        }

        @DisplayName("오염된 문자열")
        @Test
        public void pollutedString() throws Exception{
            //given
            String key = "test_secret_123456789123456789123456789123456789";
            Mockito.when(env.getProperty("token.secret"))
                    .thenReturn(key);

            String jwt = createJwt(key, "sub", "-10000");

            //when
            Boolean result = jwtService.verify(jwt + "123", "token.secret");

            //then
            assertThat(result).isFalse();
        }

        @DisplayName("secretKey가 다를때")
        @Test
        public void missMatchKey() throws Exception{
            //given
            String key = "test_secret_123456789123456789123456789123456789";
            Mockito.when(env.getProperty("token.secret"))
                    .thenReturn(key);

            String jwt = createJwt(key + "1", "sub", "-10000");

            //when
            Boolean result = jwtService.verify(jwt, "token.secret");

            //then
            assertThat(result).isFalse();
        }
    }

    private String createJwt(String key, String subject, String duration) {
        byte[] secretKeyBytes = Base64.getEncoder()
                .encode(key.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(subject)
                .expiration(Date.from(now.plusMillis(Long.parseLong(duration))))
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();
    }
}