package com.openroom.OpenRoom_backend.models;

import lombok.Data;

@Data
public class JoinRoomRequest {
    private int roomId;
    private String email;
    private String roomCode;
}
