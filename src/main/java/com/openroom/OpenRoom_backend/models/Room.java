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

    private int admin;

    // store only member IDs
    @ElementCollection
    @CollectionTable(name = "room_members", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "member_id")
    private List<Integer> membersId = new LinkedList<>();

    // store only chat IDs if you want lightweight relation
    @ElementCollection
    @CollectionTable(name = "room_chats", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "chat_id")
    private List<Integer> chatIds = new LinkedList<>();
}
