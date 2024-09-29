package org.example.library.service.implement;

import lombok.AllArgsConstructor;
import org.example.library.dto.UserDto;
import org.example.library.mapper.UserMapper;
import org.example.library.model.User;
import org.example.library.repository.UserRepository;
import org.example.library.service.UserService;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository Repo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;


    @Override
    public UserDto Register(UserDto userdto) {
        User user = UserMapper.maptoUser(userdto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = Repo.save(user);
        return UserMapper.maptoUserDto(savedUser);
    }

    @Override
    public UserDto Login(UserDto userdto) {
        Optional<User> userOptional;

        if ((userdto.getEmail() != null && !userdto.getEmail().isEmpty()) ||
                (userdto.getUserName() != null && !userdto.getUserName().isEmpty())) {
            userOptional = Repo.findByEmail(userdto.getEmail());
            if (!userOptional.isPresent()) {
                userOptional = Repo.findByUserName(userdto.getUserName());
            }
        } else {
            throw new RuntimeException("Username or email cannot be null or empty");
        }


        if (userOptional.isPresent() && passwordEncoder.matches(userdto.getPassword(), userOptional.get().getPassword())) {
            return UserMapper.maptoUserDto(userOptional.get());
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @Override
    public void ForgotPassword(UserDto userdto) {
        Optional<User> userOptional;

        if (userdto.getEmail() != null && !userdto.getEmail().isEmpty()) {
            userOptional = Repo.findByEmail(userdto.getEmail());
        } else if (userdto.getUserName() != null && !userdto.getUserName().isEmpty()) {
            userOptional = Repo.findByUserName(userdto.getUserName());
        } else {
            throw new RuntimeException("Email or username cannot be null or empty");
        }


        if (userOptional.isPresent()) {
            String token = generateToken(); // Hàm tạo token
            //link sẽ chuyển hướng đến trang đổi pass word
            String resetLink = "http://localhost:3000/reset-password?token=" + token;

            // Gửi email
            sendEmail(userOptional.get().getEmail(), "Đặt lại mật khẩu của bạn",
                    "Nhấn vào đường dẫn để đặt lại mật khẩu " + resetLink);

            userOptional.get().setResetToken(token);
            Repo.save(userOptional.get());
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> userOptional = Repo.findByResetToken(token); // Tìm người dùng theo token

        if (userOptional.isPresent()) {
            userOptional.get().setPassword(passwordEncoder.encode(newPassword));
            Repo.save(userOptional.get());
        } else {
            throw new RuntimeException("Invalid token");
        }
    }

    private String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    @Override
    public UserDto loginWithGoogle(String email, String name) {
        Optional<User> userOptional = Repo.findByEmail(email);
        if (userOptional.isPresent()) {
            return UserMapper.maptoUserDto(userOptional.get());
        } else {
            // Nếu người dùng chưa tồn tại, bạn có thể tạo một tài khoản mới
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUserName(name); // Hoặc có thể tạo một username mặc định
            newUser.setPassword(passwordEncoder.encode("defaultPassword")); // Hoặc một password mặc định khác
            User savedUser = Repo.save(newUser);
            return UserMapper.maptoUserDto(savedUser);
        }
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = Repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        Repo.save(user);
    }

}
