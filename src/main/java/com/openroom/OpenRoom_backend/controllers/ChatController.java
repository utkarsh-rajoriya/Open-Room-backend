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
        // Extract data from the map
        String message = body.get("text").toString();
        boolean isAiQuery = (boolean) body.get("ai");

        // A single call to the new, robust service method.
        // The complex if/else logic is gone from the controller.
        chatService.processUserMessage(roomId, message, clientId, email, isAiQuery);
    }

}