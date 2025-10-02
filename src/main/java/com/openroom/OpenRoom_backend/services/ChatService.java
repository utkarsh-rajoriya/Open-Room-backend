package com.openroom.OpenRoom_backend.services;
import com.openroom.OpenRoom_backend.models.ChatMessage;
import com.openroom.OpenRoom_backend.models.Member;
import com.openroom.OpenRoom_backend.models.Room;
import com.openroom.OpenRoom_backend.repositories.RoomRepo;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class ChatService {

    private SimpMessagingTemplate messagingTemplate;
    public MemberService memberService;
    public RoomRepo roomRepo;

    public ChatService(SimpMessagingTemplate messagingTemplate,RoomRepo roomRepo, MemberService memberService) {
        this.messagingTemplate = messagingTemplate;
        this.memberService = memberService;
        this.roomRepo = roomRepo;
    }

    public void sendMessage(int roomId, String message, String clientId, String email) {
        Member member = memberService.getMemberByEmail(email);
        Room room = roomRepo.findById(roomId).orElse(null);
        if(room == null) return;

        messagingTemplate.convertAndSend("/topic/room/" + roomId, new ChatMessage(member.getClientChatId() , message , member.getName(), member.getEmail(), member.getPicture()));
        room.getMessages().add(new ChatMessage(clientId,message, member.getName(), member.getEmail(), member.getPicture()));
    }
}
