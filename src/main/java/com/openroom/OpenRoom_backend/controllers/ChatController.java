package com.openroom.OpenRoom_backend.controllers;

import com.openroom.OpenRoom_backend.services.ChatService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class ChatController {

    public ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send/{roomId}")
    public void sendMessage(
            @RequestBody Map<String, Object> body,
            @PathVariable("roomId") int roomId,
            @RequestHeader("X-Client-Id") String clientId,
            @RequestHeader("email") String email
    ) {
        String message = body.get("text").toString();
        boolean ai =(boolean) body.get("ai");
        if(ai){
            chatService.sendMessageToAi(roomId, message, clientId, email);
            return;
        }
        chatService.sendMessage(roomId, message, clientId, email);
    }

}