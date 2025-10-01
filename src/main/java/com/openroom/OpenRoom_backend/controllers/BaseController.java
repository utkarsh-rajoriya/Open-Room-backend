package com.openroom.OpenRoom_backend.controllers;

import com.openroom.OpenRoom_backend.models.JoinRoomRequest;
import com.openroom.OpenRoom_backend.models.Room;
import com.openroom.OpenRoom_backend.services.BaseService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class BaseController {

    public BaseService baseService;

    public BaseController(BaseService baseService) {
        this.baseService = baseService;
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
            System.out.println("Joining room id :" + request.getRoomId() + " by <- " + request.getEmail() + " roomCode : "+ request.getRoomCode());
            return baseService.joinRoom(request.getRoomId(), request.getEmail(), request.getRoomCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new HashMap<>(Map.of("message", e.getMessage()));
        }
    }


}