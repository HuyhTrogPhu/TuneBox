package org.example.library.config;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class NotificationWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Xử lý khi kết nối thành công
        System.out.println("New connection: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, org.springframework.web.socket.TextMessage message) throws Exception {
        // Xử lý tin nhắn từ client nếu cần
        System.out.println("Received message: " + message.getPayload());
    }

    public void sendNotification(Long userId, String message) {
        // Gửi thông báo đến một phiên làm việc cụ thể
        // Lưu trữ phiên làm việc theo userId nếu cần
    }
}
