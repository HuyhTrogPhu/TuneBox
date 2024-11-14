package org.example.customer.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "tuneboxvietnamnoiamnhacthanghoalendentotdinhcuacamxucconchanchoginuamakhongdenvoichungtoi";

    @Autowired
    private JwtDecoder jwtDecoder;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        if (isGoogleToken(token)) {
            return extractClaimFromGoogleToken(token, claims -> {
                Claims internalClaims = Jwts.claims(claims);
                return claimsResolver.apply(internalClaims);
            });
        }
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private <T> T extractClaimFromGoogleToken(String token, Function<Map<String, Object>, T> claimsResolver) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            if (jwt == null) {
                throw new RuntimeException("Google token decode returned null");
            }
            Map<String, Object> claims = jwt.getClaims();
            if (claims == null || claims.isEmpty()) {
                throw new RuntimeException("No claims found in Google token");
            }
            System.out.println("Decoded claims from Google token: " + claims);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new RuntimeException("Unable to extract claims from Google token", e);
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        if (isGoogleToken(token)) {
            return extractClaim(token, claims -> claims.get("sub").toString());
        } else {
            return extractClaim(token, Claims::getSubject);
        }
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, claims -> new Date(((Number) claims.get("exp")).longValue() * 1000));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        if (isGoogleToken(token)) {
            return validateGoogleToken(token, userDetails);
        }

        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    private Boolean isGoogleToken(String token) {
        try {
            String[] tokenParts = token.split("\\.");
            if (tokenParts.length == 3) {
                String payload = new String(Base64.getDecoder().decode(tokenParts[1]));
                if (payload.contains("\"iss\":\"https://accounts.google.com\"")) {
                    return true;
                }
            }
            return false; // Token không phải của Google
        } catch (Exception e) {
            System.out.println("Error determining if token is Google token: " + e.getMessage());
            return false;
        }
    }

    private Boolean validateGoogleToken(String token, UserDetails userDetails) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String username = jwt.getClaimAsString("sub");
            System.out.println("Username from Google Token: " + username);
            System.out.println("UserDetails Username: " + userDetails.getUsername());
            return username.equals(userDetails.getUsername());
        } catch (Exception e) {
            System.out.println("Error validating Google token: " + e.getMessage());
            return false;
        }
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
}
