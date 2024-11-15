package org.example.library.repository;

import org.example.library.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByUserId(Long userId);
    void deleteByUserIdAndCode(Long userId, String code);

    VerificationCode findByToken(String token);

    void deleteByToken(String token);
}
