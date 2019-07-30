//package com.d2.timeline.config.security;
//
//import com.d2.timeline.domain.config.security.JwtTokenProvider;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.Getter;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.util.Base64;
//import java.util.Date;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.BDDMockito.given;
//
//public class JwtTokenProviderTest extends MockTest {
//
//    @InjectMocks
//    JwtTokenProvider jwtTokenProvider;
//
//    private String secretKey;
//    private long tokenValidMilisecond;
//    private String email;
//    private String role;
//    private String jwts;
//    private Claims claims;
//    private Date now;
//
//
//    @Before
//    public void setUp() throws Exception{
//        tokenValidMilisecond = 1000L * 60 * 60;
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//        now = new Date();
//        email = "test@test.com";
//        role = "USER";
//        claims = Jwts.claims().setSubject(email);
//        claims.put("role", role);
//        jwts = Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//
//    @Test
//    public void 토큰생성_성공(){
//        //given
//        //when
//        final String returnMessage = jwtTokenProvider.createToken(email, role);
//        //then
//        assertEquals(jwts, returnMessage);
//    }
//
//
//}
