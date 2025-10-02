package com.openroom.OpenRoom_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Members")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String picture;

    @Column(unique = true)
    private String email;

    private String clientChatId;

    // store joined room IDs
    @ElementCollection
    @CollectionTable(name = "member_rooms", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "room_code")
    private List<String> joinedRoomCodes = new ArrayList<>();
}