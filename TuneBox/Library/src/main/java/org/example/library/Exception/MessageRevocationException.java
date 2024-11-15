package org.example.library.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MessageRevocationException extends RuntimeException {
    public MessageRevocationException(String message) {
        super(message);
    }
}