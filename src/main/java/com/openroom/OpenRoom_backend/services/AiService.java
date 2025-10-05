package com.openroom.OpenRoom_backend.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    ChatClient chatClient;

    public AiService(@Qualifier("groqClient") ChatClient chatClient){
        this.chatClient = chatClient;
    }

    public String chat(String query) {
        String response = chatClient.prompt(query).call().content();
        return sanitizeResponse(response);
    }

    private String sanitizeResponse(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }
}
