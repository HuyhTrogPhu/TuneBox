package org.example.library.service;

public interface SendAlbumService {
    void sendAlbumMessage(Long senderId, Long receiverId, Long albumId);
}
