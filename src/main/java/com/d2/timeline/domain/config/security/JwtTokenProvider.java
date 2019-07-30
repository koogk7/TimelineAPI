package com.d2.timeline.domain.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

//todo

/**
 *  JwtTokenProvider : JWT 토큰을 생성 및 검증하는 모듈
 */

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Getter
    @Value("${spring.jwt.secret}")
    private String secretKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);


    private long tokenValidMilisecond = 1000L * 60 * 60; // 1시간

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email, String role){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.
                loadUserByUsername(this.getUsePk(token));

        return new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities());
    }

    public String getUsePk(String token){
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        System.out.println(request.toString());
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e){
            return false;
        }
    }

}