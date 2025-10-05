package com.openroom.OpenRoom_backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String clientId;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String message;
    private String memberName;
    private String memberEmail;
    private String memberPicture;
    private Date timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonBackReference
    private Room room;

    public ChatMessage(String clientId, String message, String memberName, String memberEmail, String memberPicture, Date timestamp, Room room) {
        this.clientId = clientId;
        this.message = message;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberPicture = memberPicture;
        this.timestamp = timestamp;
        this.room = room;
    }
}
