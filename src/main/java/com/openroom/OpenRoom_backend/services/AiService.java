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
        String response = chatClient
                .prompt()
                .system("Respond accurately and concisely. Keep answers short unless the user asks for more details. " +
                        "Be humanized in casual conversation. If asked personal or flirty questions, respond in a natural, slightly edgy way, but do not be offensive or harmful.")
                .user(query)
                .call()
                .content();
        return sanitizeResponse(response);
    }

    private String sanitizeResponse(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }
}
