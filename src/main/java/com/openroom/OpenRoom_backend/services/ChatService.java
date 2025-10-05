package com.openroom.OpenRoom_backend.services;
import com.openroom.OpenRoom_backend.models.ChatMessage;
import com.openroom.OpenRoom_backend.models.ChatMessageDto;
import com.openroom.OpenRoom_backend.models.Member;
import com.openroom.OpenRoom_backend.models.Room;
import com.openroom.OpenRoom_backend.repositories.ChatRepo;
import com.openroom.OpenRoom_backend.repositories.RoomRepo;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private SimpMessagingTemplate messagingTemplate;
    public MemberService memberService;
    public RoomRepo roomRepo;
    public ChatRepo chatRepo;

    public ChatService(SimpMessagingTemplate messagingTemplate,RoomRepo roomRepo, MemberService memberService, ChatRepo chatRepo) {
        this.messagingTemplate = messagingTemplate;
        this.memberService = memberService;
        this.roomRepo = roomRepo;
        this.chatRepo = chatRepo;
    }

    public void sendMessage(int roomId, String message, String clientId, String email) {
        Member member = memberService.getMemberByEmail(email);
        Room room = roomRepo.findById(roomId).orElse(null);
        if(room == null) return;

        messagingTemplate.convertAndSend("/topic/room/" + roomId, new ChatMessage(member.getClientChatId() , message , member.getName(), member.getEmail(), member.getPicture(), new Date(), room));
        room.getMessages().add(new ChatMessage(clientId,message, member.getName(), member.getEmail(), member.getPicture(), new Date(), room));
        roomRepo.save(room);
    }

    public void sendMessageToAi(int roomId, String message, String clientId, String email) {
        Member member = memberService.getMemberByEmail(email);
        Room room = roomRepo.findById(roomId).orElse(null);
        if(room == null) return;

        messagingTemplate.convertAndSend("/topic/room/" + roomId, new ChatMessage(member.getClientChatId() , message , member.getName(), member.getEmail(), member.getPicture(), new Date(), room));
        room.getMessages().add(new ChatMessage(clientId,message, member.getName(), member.getEmail(), member.getPicture(), new Date(), room));
        roomRepo.save(room);
    }

    @Transactional(readOnly = true)
    public Page<ChatMessageDto> getChatsDto(int roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").ascending());

        Page<ChatMessage> chats = chatRepo.findByRoomId(roomId, pageable);

        List<ChatMessageDto> chatDtos = chats.stream().map(chat -> {
            ChatMessageDto dto = new ChatMessageDto();
            dto.setClientId(chat.getClientId());
            dto.setMessage(chat.getMessage());
            dto.setMemberName(chat.getMemberName());
            dto.setMemberPicture(chat.getMemberPicture());
            dto.setTimestamp(chat.getTimestamp());
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(chatDtos, pageable, chats.getTotalElements());
    }
}
