package com.openroom.OpenRoom_backend.controllers;

import com.openroom.OpenRoom_backend.models.*;
import com.openroom.OpenRoom_backend.services.AiService;
import com.openroom.OpenRoom_backend.services.BaseService;
import com.openroom.OpenRoom_backend.services.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class BaseController {

    public BaseService baseService;
    public ChatService chatService;
    public AiService aiService;

    public BaseController(BaseService baseService, ChatService chatService, AiService aiService) {
        this.baseService = baseService;
        this.chatService = chatService;
        this.aiService = aiService;
    }

    @GetMapping("testing1")
    public String testing1(){
        return "testing public url";
    }

    @GetMapping("testing2")
    public String testing2(){
        return "testing private url";
    }

    @PostMapping("createRoom")
    public Map<String , Object> createRoom(@RequestBody Room room , @RequestParam("email") String email){
        try{
            return baseService.createRoom(room,email);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new HashMap<>(Map.of("message" , e.getMessage()));
        }
    }

    @PostMapping("joinRoom")
    public Map<String , Object> joinRoom(@RequestBody JoinRoomRequest request) {
        try {
            return baseService.joinRoom(request.getRoomId(), request.getEmail(), request.getRoomCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new HashMap<>(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("getChats/{roomId}")
    public Page<ChatMessageDto> getChats(@PathVariable("roomId") int roomId, @RequestParam(value = "page", defaultValue = "0") int page , @RequestParam(value = "size", defaultValue = "10") int size){
        return chatService.getChatsDto(roomId, page , size);
    }

    @GetMapping("getRooms")
    public Map<String , Object> getRooms(@RequestParam("email") String email){
        return baseService.getRooms(email);
    }

    @PostMapping("checkUserValidity")
    public Map<String , Object> checkUserValidity(@RequestBody Map<String,Object> map){
        int roomId = Integer.parseInt(map.get("roomId").toString());
        String email = map.get("email").toString();

        return baseService.checkUserValidity(roomId, email);
    }

    @GetMapping("roomMembersInfo")
    public Map<String,Object> roomMembersInfo(@RequestParam("roomId") int roomId){
        return baseService.roomMembersInfo(roomId);
    }

//    @PostMapping("ai/chat")
//    public Flux<String> chat(@RequestParam("query") String query){
//        return aiService.chat(query);
//    }
}