package com.openroom.OpenRoom_backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessage {
    private String clientId;
    private String text;

    public ChatMessage(String clientId, String text) {
        this.clientId = clientId;
        this.text = text;
    }
}
