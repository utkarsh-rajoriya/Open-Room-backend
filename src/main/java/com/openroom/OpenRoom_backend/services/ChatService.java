package com.openroom.OpenRoom_backend.services;

import com.openroom.OpenRoom_backend.models.ChatMessage;
import com.openroom.OpenRoom_backend.models.ChatMessageDto;
import com.openroom.OpenRoom_backend.models.Member;
import com.openroom.OpenRoom_backend.models.Room;
import com.openroom.OpenRoom_backend.repositories.ChatRepo;
import com.openroom.OpenRoom_backend.repositories.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberService memberService;
    private final RoomRepo roomRepo;
    private final ChatRepo chatRepo;
    private final AiService aiService;
    private final ChatService self;

    @Autowired
    public ChatService(SimpMessagingTemplate messagingTemplate, MemberService memberService,
                       RoomRepo roomRepo, ChatRepo chatRepo, AiService aiService, @Lazy ChatService self) {
        this.messagingTemplate = messagingTemplate;
        this.memberService = memberService;
        this.roomRepo = roomRepo;
        this.chatRepo = chatRepo;
        this.aiService = aiService;
        this.self = self;
    }

    @Transactional
    public void processUserMessage(int roomId, String message, String clientId, String email, boolean isAiQuery) {
        Member member = memberService.getMemberByEmail(email);
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room with ID " + roomId + " not found."));

        ChatMessage userMessage = new ChatMessage(
                clientId, message, member.getName(),
                member.getEmail(), member.getPicture(), new Date(), room
        );

        room.getMessages().add(userMessage);
        roomRepo.save(room);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, userMessage);

        if (isAiQuery) {
            self.fetchAndProcessAiResponse(roomId, message);
        }
    }

    @Async
    @Transactional
    public void fetchAndProcessAiResponse(int roomId, String userMessage) {
        Room room = roomRepo.findById(roomId).orElse(null);
        if (room == null) {
            System.err.println("Cannot send AI response. Room not found for ID: " + roomId);
            return;
        }

        try {
            String aiResponse = aiService.chat(userMessage);
            ChatMessage aiMessage = new ChatMessage(
                    "Ai", aiResponse, "AI Assistant", "ai@openroom",
                    "https://cdn-icons-png.flaticon.com/512/4712/4712109.png", new Date(), room
            );

            room.getMessages().add(aiMessage);
            roomRepo.save(room);

            messagingTemplate.convertAndSend("/topic/room/" + roomId, aiMessage);
        } catch (Exception e) {
            System.err.println("Error getting AI response for room " + roomId + ": " + e.getMessage());
            ChatMessage errorMessage = new ChatMessage(
                    "Ai", "Sorry, an error occurred while processing your request.", "AI Assistant",
                    "ai@openroom", "https://cdn-icons-png.flaticon.com/512/4712/4712109.png", new Date(), room
            );
            messagingTemplate.convertAndSend("/topic/room/" + roomId, errorMessage);
        }
    }

    @Transactional(readOnly = true)
    public Page<ChatMessageDto> getChatsDto(int roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<ChatMessage> chats = chatRepo.findByRoom_Id(roomId, pageable);
        List<ChatMessageDto> chatDtos = chats.stream().map(this::convertToDto).collect(Collectors.toList());
        return new PageImpl<>(chatDtos, pageable, chats.getTotalElements());
    }

    private ChatMessageDto convertToDto(ChatMessage chat) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setClientId(chat.getClientId());
        dto.setMessage(chat.getMessage());
        dto.setMemberName(chat.getMemberName());
        dto.setMemberPicture(chat.getMemberPicture());
        dto.setTimestamp(chat.getTimestamp());
        return dto;
    }
}