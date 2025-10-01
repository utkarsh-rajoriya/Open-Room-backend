package com.openroom.OpenRoom_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    private int senderId;

    private int roomId;
}