package org.example.library.service.implement;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.example.library.dto.UserDto;
import org.example.library.dto.UserInformationDto;
import org.example.library.mapper.UserMapper;
import org.example.library.model.*;
import org.example.library.repository.*;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private InspiredByRepository inspiredByRepository;


    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private Cloudinary cloudinary;

    private final JavaMailSender javaMailSender;


    @Override
    public UserDto register(UserDto userDto, UserInformationDto userInformationDto, MultipartFile image) {
        try {
            User user = new User();
            UserInformation userInformation = new UserInformation();

            userInformation.setName(userInformationDto.getName());

            Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");
            userInformation.setAvatar(imageUrl);
            userInformation.setGender(userInformationDto.getGender());
            userInformation.setAbout(userInformationDto.getAbout());
            userInformation.setBackground(userInformationDto.getBackground());
            userInformation.setBirthDay(userInformationDto.getBirthDay());
            userInformation.setPhoneNumber(userInformationDto.getPhoneNumber());

            user.setUserInformation(userInformation);

            user.setRole(roleRepository.findByName("CUSTOMER"));
            user.setEmail(userDto.getEmail());
            user.setUserName(userDto.getUserName());
            user.setPassword(userDto.getPassword());
            user.setReport(false);
            user.setCreateDate(LocalDate.now());

            // Map InspiredBy từ danh sách ID trong DTO
            Set<InspiredBy> inspiredBySet = new HashSet<>();
            for (Long inspiredId : userDto.getInspiredBy()) {
                InspiredBy inspiredBy = inspiredByRepository.findById(inspiredId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inspired by not found"));
                inspiredBySet.add(inspiredBy);
            }
            user.setInspiredBy(inspiredBySet);

            // Map Talent từ danh sách ID trong DTO
            Set<Talent> talentSet = new HashSet<>();
            for (Long talentId : userDto.getTalent()) {
                Talent talent = talentRepository.findById(talentId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Talent not found"));
                talentSet.add(talent);
            }
            user.setTalent(talentSet);

            // Map Genre từ danh sách ID trong DTO
            Set<Genre> genreSet = new HashSet<>();
            for (Long genreId : userDto.getGenre()) {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found"));
                genreSet.add(genre);
            }
            user.setGenre(genreSet);

            userRepository.save(user);

            return UserMapper.mapToUserDto(user);

        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }

    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }


    @Override
    public UserDto loginWithGoogle(UserDto userdto) {
//        Optional<User> userOptional;
//
//        if ((userdto.getEmail() != null && !userdto.getEmail().isEmpty()) ||
//                (userdto.getUserName() != null && !userdto.getUserName().isEmpty())) {
//            userOptional = userRepository.findByEmail(userdto.getEmail());
//            if (!userOptional.isPresent()) {
//                userOptional = userRepository.findByUserName(userdto.getUserName());
//            }
//        } else {
//            throw new RuntimeException("Username or email cannot be null or empty");
//        }
//
//        if (userOptional.isPresent() && passwordEncoder.matches(userdto.getPassword(), userOptional.get().getPassword())) {
//            return UserMapper.mapToUserDto(userOptional.get());
//        } else {
//            throw new RuntimeException("Invalid username or password");
//        }
        return null;
    }



    public void resetPassword(UserDto userdto) {

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

    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email);

        if (!oldPassword.matches(newPassword)) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return null;
    }

    @Override
    public UserDto findUserById(Long userId) {
       return null;
    }


    @Override
    public void forgotPassword(UserDto userdto) {

    }

    @Override
    public void resetPassword(String token, String newPassword) {

    }


}