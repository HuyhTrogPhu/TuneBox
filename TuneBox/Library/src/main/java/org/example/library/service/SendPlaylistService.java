package org.example.library.service;

public interface SendPlaylistService {
    void sendPlaylistMessage(Long senderId, Long receiverId, Long playlistId);
}
