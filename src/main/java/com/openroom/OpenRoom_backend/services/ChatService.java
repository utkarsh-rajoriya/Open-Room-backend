package com.openroom.OpenRoom_backend.services;
import com.openroom.OpenRoom_backend.models.ChatMessage;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class ChatService {

    private SimpMessagingTemplate messagingTemplate;

    public ChatService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(int roomId, String message, String clientId) {
        messagingTemplate.convertAndSend("/topic/room/" + roomId, new ChatMessage(clientId, message));
    }
}
