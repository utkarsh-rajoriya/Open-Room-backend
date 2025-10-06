package com.openroom.OpenRoom_backend.models;

import lombok.AllArgsConstructor;import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class  RoomDto {
    int id;
    String name;
    int currentMembers;
    int memberLimit;
    boolean privacy;
    boolean ai;
    String roomCode;
    int adminId;

    public RoomDto(int id, String name, int memberLimit, boolean privacy, boolean ai, String roomCode, int adminId, int currentMembers) {
        this.id = id;
        this.name = name;
        this.currentMembers = currentMembers;
        this.memberLimit = memberLimit;
        this.privacy = privacy;
        this.ai = ai;
        this.roomCode = roomCode;
        this.adminId = adminId;
    }
}
