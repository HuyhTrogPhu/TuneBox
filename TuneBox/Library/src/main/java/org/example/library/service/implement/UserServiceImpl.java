package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.dto.RequestSignUpModel;
import org.example.library.dto.UserDto;
import org.example.library.mapper.UserMapper;
import org.example.library.model.Genre;
import org.example.library.model.InspiredBy;
import org.example.library.model.Talent;
import org.example.library.model.User;
import org.example.library.repository.*;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository Repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private InspiredByRepository inspiredByRepository;


    @Autowired
    private TalentRepository TalentRepo;

    @Autowired
    private GenreRepository GenreRepo;


//    private PasswordEncoder crypt;

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final RoleRepository roleRepository;


    @Override
    public void CheckLogin(RequestSignUpModel requestSignUpModel) {
        List<User> fullList = Repo.findAll();

        for (User user : fullList) {
            if (user.getEmail().equals(requestSignUpModel.getUserDto().getEmail())) {
                System.out.println("trùng Email: " + requestSignUpModel.getUserDto().getEmail());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");
            }
            if (user.getUserName().equals(requestSignUpModel.getUserDto().getUserName())) {
                System.out.println("trùng Username: " + requestSignUpModel.getUserDto().getUserName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username đã tồn tại");
            }
        }
    }

    @Override
    public UserDto Register(RequestSignUpModel requestSignUpModel) {
        User savedUser = new User();

        savedUser.setEmail(requestSignUpModel.getUserDto().getEmail());
        savedUser.setPassword(passwordEncoder.encode(requestSignUpModel.getUserDto().getPassword()));
        savedUser.setUserName(requestSignUpModel.getUserDto().getUserName());
        savedUser.setUserNickname(requestSignUpModel.getUserDto().getUserNickname());


        List<InspiredBy> inspiredByList = new ArrayList<>();
        List<Talent> talentList = new ArrayList<>();
        List<Genre> genreList = new ArrayList<>();

        for (String inspiredByName1 : requestSignUpModel.getListInspiredBy()) {
            List<InspiredBy> inspiredBy = inspiredByRepository.findByName(inspiredByName1);
            if (inspiredBy != null) {
                inspiredByList.addAll(inspiredBy);
            }
        }


        for (String talentName : requestSignUpModel.getListTalent()) {
            List<Talent> talent = TalentRepo.findByName(talentName);
            if (talent != null) {
                talentList.addAll(talent);
            }
        }


        for (String genreName : requestSignUpModel.getGenreBy()) {
            List<Genre> genre = GenreRepo.findByName(genreName);
            if (genre != null) {
                genreList.addAll(genre);
            }
        }


        savedUser.setInspiredBy(Set.copyOf(inspiredByList));
        savedUser.setTalent(Set.copyOf(talentList));
        savedUser.setGenre(Set.copyOf(genreList));

        savedUser.setRole(roleRepo.findByName("CUSTOMER"));


        Repo.save(savedUser);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Repo.findById(userId);
    }

    public User updateById(Long userId, UserDto userDto) {
        return Repo.save(UserMapper.mapToUser(userDto));
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
            return UserMapper.mapToUserDto(userOptional.get());
        } else {
            throw new RuntimeException("Invalid username or password");
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

            return UserMapper.mapToUserDto((userOptional.get()));
        } else {
            // Nếu người dùng chưa tồn tại, bạn có thể tạo một tài khoản mới
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUserName(name); // Hoặc có thể tạo một username mặc định
            newUser.setPassword(passwordEncoder.encode("defaultPassword")); // Hoặc một password mặc định khác
            User savedUser = Repo.save(newUser);

            return UserMapper.mapToUserDto(savedUser);
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

    public UserDto getUserById(Long id) {
        return Repo.findById(id)
                .map(user -> new UserDto(user.getId(), user.getUserName())) // Chuyển đổi sang UserDto
                .orElseThrow(() -> new RuntimeException("User not found"));
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


}
