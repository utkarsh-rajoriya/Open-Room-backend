package com.openroom.OpenRoom_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "members")
public class Member {
    private String id;
    private String name;
    private String email;
    private List<String> joinedERoomId;
}
