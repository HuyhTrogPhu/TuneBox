package org.example.library.Exception;

// Lớp ngoại lệ tùy chỉnh
public class UserNotFoundException extends Exception {

    // Constructor với thông báo lỗi
    public UserNotFoundException(String message) {
        super(message);
    }

    // Constructor với thông báo lỗi và nguyên nhân gây ra ngoại lệ
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

