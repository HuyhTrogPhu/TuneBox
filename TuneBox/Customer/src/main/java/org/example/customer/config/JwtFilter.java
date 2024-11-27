package org.example.customer.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.library.model_enum.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("Request URI: " + path);

        if (!path.equals("/login") && !path.equals("/register")) {
            String authorizationHeader = request.getHeader("Authorization");
            System.out.println("Authorization Header: " + authorizationHeader);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                try {
                    boolean isGoogleToken = jwtUtil.isGoogleToken(jwt);
                    String username = jwtUtil.extractUsername(jwt);
                    System.out.println("Extracted Username: " + username);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        System.out.println("User Details: " + userDetails);

                        // **Kiểm tra trạng thái người dùng**
                        if (userDetails instanceof CustomerDetail) { // Kiểm tra xem UserDetails có phải là CustomerDetail không
                            CustomerDetail customerDetail = (CustomerDetail) userDetails;
                            if (customerDetail.getUser().getStatus() == UserStatus.BANNED) {
                                System.out.println("User is banned");
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tài khoản đã bị khóa.");
                                return;
                            }
                        }

                        if (jwtUtil.validateToken(jwt, userDetails)) {
                            Collection<? extends GrantedAuthority> authorities;
                            if (isGoogleToken) {
                                // Lấy quyền từ Google token
                                authorities = jwtUtil.extractGoogleRoles(jwt);
                            } else {
                                // Lấy quyền từ token của hệ thống
                                authorities = userDetails.getAuthorities();
                            }

                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            System.out.println("Authentication Successful");
                        } else {
                            System.out.println("Invalid JWT token");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                            return;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error decoding JWT token: " + e.getMessage());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
}

