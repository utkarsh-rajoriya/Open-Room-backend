package com.openroom.OpenRoom_backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String clientId;
    private String message;
    private String memberName;
    private String memberEmail;
    private String memberPicture;

    public ChatMessage(String clientId, String message, String memberName, String memberEmail, String memberPicture) {
        this.clientId = clientId;
        this.message = message;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberPicture = memberPicture;
    }
}
