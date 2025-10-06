package com.openroom.OpenRoom_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MembersInfoDTO {
    int id;
    String name;
    String email;
    String picture;
}
