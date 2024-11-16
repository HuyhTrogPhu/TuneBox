package org.example.library.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MessageRevocationTimeoutException extends RuntimeException {
    public MessageRevocationTimeoutException(String message) {
        super(message);
    }
}