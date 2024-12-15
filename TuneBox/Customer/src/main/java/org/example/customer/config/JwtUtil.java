package org.example.customer.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "tuneboxvietnamnoiamnhacthanghoalendentotdinhcuacamxucconchanchoginuamakhongdenvoichungtoi";

    @Autowired
    private JwtDecoder jwtDecoder;

    // Trích xuất username từ token
    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("sub").toString());
    }

    // Trích xuất ngày hết hạn từ token
    public Date extractExpiration(String token) {
        return extractClaim(token, claims -> {
            Number expiration = (Number) claims.get("exp");
            return new Date(expiration.longValue() * 1000);
        });
    }

    // Trích xuất claim cụ thể từ token
    public <T> T extractClaim(String token, Function<Map<String, Object>, T> claimsResolver) {
        if (isGoogleToken(token)) {
            return extractClaimFromGoogleToken(token, claimsResolver);
        }
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Giải mã và lấy toàn bộ claims từ token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).setAllowedClockSkewSeconds(60) // Cung cấp thêm clock skew nếu cần
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Kiểm tra token có hết hạn không
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra tính hợp lệ của token đối với người dùng
    public Boolean validateToken(String token, UserDetails userDetails) {
        if (isGoogleToken(token)) {
            return validateGoogleToken(token, userDetails);
        }

        final String usernameFromToken = extractUsername(token).split("@")[0];
        final String usernameFromUserDetails = userDetails.getUsername();

        System.out.println("Validating token for username from token: " + usernameFromToken);
        System.out.println("Validating token for username from userDetails: " + usernameFromUserDetails);

        boolean tokenValid = usernameFromToken.equals(usernameFromUserDetails) && !isTokenExpired(token);
        System.out.println("Is Token Valid: " + tokenValid);

        return tokenValid;
    }



    // Kiểm tra nếu token là của Google
    Boolean isGoogleToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String issuer = jwt.getClaimAsString("iss");
            return issuer != null && issuer.contains("accounts.google.com");
        } catch (Exception e) {
            return false;
        }
    }

    // Giải mã và trích xuất claim từ Google token
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
            System.out.println("Decoded claims from Google token: " + claims); // In ra claims để kiểm tra
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new RuntimeException("Unable to extract claims from Google token", e);
        }
    }

    private Boolean validateGoogleToken(String token, UserDetails userDetails) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String username = jwt.getClaimAsString("sub");
            System.out.println("Username from Google Token: " + username);
            System.out.println("UserDetails Username: " + userDetails.getUsername());

            boolean tokenValid = username.equals(userDetails.getUsername());
            System.out.println("Is Google Token Valid: " + tokenValid);

            return tokenValid;
        } catch (Exception e) {
            System.out.println("Error validating Google token: " + e.getMessage());
            return false;
        }
    }

    public Collection<? extends GrantedAuthority> extractGoogleRoles(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        List<String> roles = jwt.getClaimAsStringList("scope");
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    // Tạo JWT
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token 10 giờ
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    // Trích xuất vai trò từ token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
