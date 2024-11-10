package org.example.library.service;

import org.example.library.model.User;
import org.example.library.model.VerificationCode;
import org.example.library.repository.UserRepository;
import org.example.library.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
@Autowired
private UserRepository userRepository;
    public void save(VerificationCode verificationCode) {
        verificationCodeRepository.save(verificationCode);
    }

    public boolean isValidCode(String email, String code) {
        // Lấy userId từ email
        User user = userRepository.findByEmail(email);

        if (user == null) { // Kiểm tra xem người dùng có tồn tại không
            throw new RuntimeException("User not found");
        }

        Long userId = user.getId(); // Lấy userId từ đối tượng User
        VerificationCode verificationCode = verificationCodeRepository.findByUserId(userId);

        return verificationCode != null && verificationCode.getCode().equals(code);
    }

    public void deleteCode(String email, String code) {
        // Lấy userId từ email
        User user = userRepository.findByEmail(email);

        if (user == null) { // Kiểm tra xem người dùng có tồn tại không
            throw new RuntimeException("User not found");
        }

        Long userId = user.getId(); // Lấy userId từ đối tượng User
        verificationCodeRepository.deleteByUserIdAndCode(userId, code);
    }

    public VerificationCode findByToken(String token) {
        System.out.println("Finding VerificationCode with token: " + token);
        return verificationCodeRepository.findByToken(token);
    }

    public void deleteToken(String token) {
        verificationCodeRepository.deleteByToken(token);
    }
}
