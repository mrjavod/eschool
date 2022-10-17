package com.home.eschool.security.jwt;

import com.home.eschool.entity.Users;
import com.home.eschool.security.JwtUserDetailsService;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtTokenProvider {

    private String secret = "spring-app-for-school";
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtTokenProvider(JwtUserDetailsService jwtUserDetailsService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * @param user Users
     * @return Users
     */
    public String generateAccessToken(Users user) {

        long expiresIn = 43200000;

        Claims claims = Jwts.claims().setSubject(user.getLogin());
        claims.put("id", user.getId());
        claims.put("full_name", user.getFullName());
        claims.put("role", user.getRole());

        Date now = new Date();
        Date validity = new Date(now.getTime() + expiresIn);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * @param token String
     * @return Authentication
     */
    Authentication getAuthentication(String token) {
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * @param token String
     * @return String
     */
    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * @param request HttpServletRequest
     * @return String
     */
    String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    /**
     * @param token String
     * @return boolean
     * @throws JwtException             JwtException
     * @throws IllegalArgumentException IllegalArgumentException
     */
    boolean validateToken(String token) throws JwtException, IllegalArgumentException {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }
}
