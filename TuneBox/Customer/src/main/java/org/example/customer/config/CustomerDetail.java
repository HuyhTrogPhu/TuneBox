
package org.example.customer.config;


import org.example.library.model.User;
import org.example.library.model_enum.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CustomerDetail implements UserDetails {

    private final User user;

    // Constructor nhận User
    public CustomerDetail(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (user != null && user.getRole() != null && user.getRole().getName() != null) {
            authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Luôn không hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        // Tài khoản không bị khóa nếu trạng thái không phải là BANNED
        return user.getStatus() != UserStatus.BANNED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Luôn không hết hạn
    }

    @Override
    public boolean isEnabled() {
        // Tài khoản được kích hoạt nếu trạng thái là ACTIVE
        return user.getStatus() == UserStatus.ACTIVE;
    }
}
