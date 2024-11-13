package org.example.customer.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "tuneboxvietnamnoiamnhacthanghoalendentotdinhcuacamxucconchanchoginuamakhongdenvoichungtoi";

    @Autowired
    private JwtDecoder jwtDecoder; // Sử dụng JwtDecoder cho token Google

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("sub").toString());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, claims -> new Date((Long) claims.get("exp") * 1000));
    }

    public <T> T extractClaim(String token, Function<Map<String, Object>, T> claimsResolver) {
        if (isGoogleToken(token)) {
            return extractClaimFromGoogleToken(token, claimsResolver); // Sử dụng phương thức cho Google token
        }

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // Xác thực token của bạn sử dụng SECRET_KEY
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        if (isGoogleToken(token)) {
            return validateGoogleToken(token, userDetails); // Xác thực Google token
        }

        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isGoogleToken(String token) {
        // Kiểm tra nếu token là của Google (có thể dựa trên cấu trúc hoặc claim)
        return token.contains(".");
    }

    private <T> T extractClaimFromGoogleToken(String token, Function<Map<String, Object>, T> claimsResolver) {
        try {
            // Giải mã Google token
            Jwt jwt = jwtDecoder.decode(token);

            if (jwt == null) {
                throw new RuntimeException("Google token decode returned null");
            }

            // Lấy toàn bộ claims từ Google token và truyền trực tiếp vào claimsResolver
            Map<String, Object> claims = jwt.getClaims();

            if (claims == null || claims.isEmpty()) {
                throw new RuntimeException("No claims found in Google token");
            }

            // In ra claims để kiểm tra giá trị
            System.out.println("Decoded claims from Google token: " + claims);

            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new RuntimeException("Unable to extract claims from Google token", e);
        }
    }

    private Boolean validateGoogleToken(String token, UserDetails userDetails) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String username = jwt.getClaimAsString("sub"); // Lấy thông tin người dùng từ token Google
            return username.equals(userDetails.getUsername());
        } catch (Exception e) {
            return false; // Nếu có lỗi trong quá trình xác thực
        }
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token 10 giờ
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);  // Lấy thông tin vai trò
    }
}
