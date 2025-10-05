package com.openroom.OpenRoom_backend.models;

import lombok.Data;

import java.util.Date;

@Data

public class ChatMessageDto {
    private String clientId;
    private String message;
    private String memberName;
    private String memberPicture;
    private Date timestamp;
}

