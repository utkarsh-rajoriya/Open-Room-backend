package com.openroom.OpenRoom_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "Rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private int memberLimit;
    private boolean privacy;
    private boolean ai;

    @Column(unique = true, nullable = false, length = 7)
    private String roomCode;

    private int adminId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new LinkedList<>();
}
