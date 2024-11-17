package org.example.library.service;

public interface SendInstrumentService {
    void sendInstrumentMessage(Long senderId, Long receiverId, Long productId);
}
