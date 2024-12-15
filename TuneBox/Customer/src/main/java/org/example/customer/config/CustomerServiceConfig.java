package org.example.customer.config;


import jakarta.transaction.Transactional;
import org.example.library.model.User;
import org.example.library.model_enum.UserStatus;
import org.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class    CustomerServiceConfig implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm kiếm người dùng bằng email trước
        Optional<User> userOptional = userRepository.findOptionalByEmail(username);

        // Nếu không tìm thấy bằng email, tìm kiếm bằng username (trước dấu @)
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByUserName(username.split("@")[0]);
        }

        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email: " + username));

        // Kiểm tra trạng thái tài khoản
        if (user.getStatus() == UserStatus.BANNED) {
            throw new RuntimeException("Account is banned");
        }

        System.out.println("User found: " + user.getUserName());
        return new CustomerDetail(user);
    }

}