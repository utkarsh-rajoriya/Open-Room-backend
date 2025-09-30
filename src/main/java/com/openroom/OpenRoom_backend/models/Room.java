package com.openroom.OpenRoom_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "rooms")
public class Room {
    private String id;
    private String name;
    private int limit;
    private boolean privacy;
    private boolean ai;
    private List<Member> members;
}
