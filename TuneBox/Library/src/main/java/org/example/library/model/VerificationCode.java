package org.example.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Setter
@Getter
@Entity
public class VerificationCode {
    // Getter và Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String code;
    private String token;
    // Constructor mặc định
    public VerificationCode() {
    }

    // Constructor với các tham số
    public VerificationCode(Long userId, String code, String token) {
        this.userId = userId;
        this.code = code;
        this.token = token;
    }


}
