package org.example.library.service;

public interface SendTrackService {
    void sendTrackMessage(Long senderId, Long receiverId, Long trackId);
}
