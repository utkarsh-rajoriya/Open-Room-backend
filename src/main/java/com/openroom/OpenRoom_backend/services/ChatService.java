package com.openroom.OpenRoom_backend.services;

import com.openroom.OpenRoom_backend.models.ChatMessage;
import com.openroom.OpenRoom_backend.models.ChatMessageDto;
import com.openroom.OpenRoom_backend.models.Member;
import com.openroom.OpenRoom_backend.models.Room;
import com.openroom.OpenRoom_backend.repositories.ChatRepo;
import com.openroom.OpenRoom_backend.repositories.RoomRepo;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberService memberService;
    private final RoomRepo roomRepo;
    private final ChatRepo chatRepo;
    private final AiService aiService;

    public ChatService(SimpMessagingTemplate messagingTemplate,
                       RoomRepo roomRepo,
                       MemberService memberService,
                       ChatRepo chatRepo,
                       AiService aiService) {
        this.messagingTemplate = messagingTemplate;
        this.memberService = memberService;
        this.roomRepo = roomRepo;
        this.chatRepo = chatRepo;
        this.aiService = aiService;
    }

    public void sendMessage(int roomId, String message, String clientId, String email) {
        Member member = memberService.getMemberByEmail(email);
        Room room = roomRepo.findById(roomId).orElse(null);
        if (room == null) return;

        ChatMessage userMessage = new ChatMessage(
                clientId,
                message,
                member.getName(),
                member.getEmail(),
                member.getPicture(),
                new Date(),
                room
        );
        messagingTemplate.convertAndSend("/topic/room/" + roomId, userMessage);

        room.getMessages().add(userMessage);
        roomRepo.save(room);
    }

    @Async
    @Transactional
    public void sendMessageToAi(int roomId, String message, String clientId, String email) {
        Member member = memberService.getMemberByEmail(email);
        Room room = roomRepo.findById(roomId).orElse(null);
        if (room == null) return;

        ChatMessage userMessage = new ChatMessage(
                clientId,
                message,
                member.getName(),
                member.getEmail(),
                member.getPicture(),
                new Date(),
                room
        );
        messagingTemplate.convertAndSend("/topic/room/" + roomId, userMessage);
        room.getMessages().add(userMessage);
        roomRepo.save(room);

        try {
            TimeUnit.SECONDS.sleep(1);

            String aiResponse = aiService.chat(message);

            ChatMessage aiMessage = new ChatMessage(
                    "Ai",
                    aiResponse,
                    "ai@openroom",
                    "ai@groq.com",
                    "https://cdn-icons-png.flaticon.com/512/4712/4712109.png",
                    new Date(),
                    room
            );

            room.getMessages().add(aiMessage);
            roomRepo.save(room);

            messagingTemplate.convertAndSend("/topic/room/" + roomId, aiMessage);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
