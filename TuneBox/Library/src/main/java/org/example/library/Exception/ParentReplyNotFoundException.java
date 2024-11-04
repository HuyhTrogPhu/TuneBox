package org.example.library.Exception;

public class ParentReplyNotFoundException extends RuntimeException {
    public ParentReplyNotFoundException(String message) {
        super(message);
    }
}