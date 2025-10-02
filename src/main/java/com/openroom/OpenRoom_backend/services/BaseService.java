package com.openroom.OpenRoom_backend.services;

import com.openroom.OpenRoom_backend.models.Member;
import com.openroom.OpenRoom_backend.models.Room;
import com.openroom.OpenRoom_backend.repositories.MemberRepo;
import com.openroom.OpenRoom_backend.repositories.RoomRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BaseService {
    public RoomRepo roomRepo;
    public MemberService memberService;
    public MemberRepo memberRepo;

    public BaseService(MemberService memberService, RoomRepo roomRepo, MemberRepo memberRepo) {
        this.memberService = memberService;
        this.roomRepo = roomRepo;
        this.memberRepo = memberRepo;
    }

    public Map<String, Object> createRoom(Room room,String email ) {
        Room rm = roomRepo.findByName(room.getName());
        if(rm != null){
            return new HashMap<>(Map.of(
                    "message" , "Room already exist with this name"
            ));
        }

        Member member = memberService.getMemberByEmail(email);
        if(member == null) return new HashMap<>(Map.of("message" , "user not found login again"));

        String roomCode = UUID.randomUUID().toString().replace("-" , "").substring(0,7).toUpperCase();
        room.setRoomCode(roomCode);
        room.setAdminId(member.getId());
        room.getMembers().add(member);
        Room savedRoom = roomRepo.save(room);

        return new HashMap<>(Map.of(
                "message" , "success",
                "room" , savedRoom
        ));
    }

    public Map<String, Object> joinRoom(int roomId, String email, String roomCode) {
        Member member = memberService.getMemberByEmail(email);
        Room room = roomRepo.findById(roomId).orElse(null);

        if(room == null){
            return new HashMap<>(Map.of("message" , "room not exist"));
        }

        if(member != null){
            if(!room.isPrivacy()){
                //put member to room
                room.getMembers().add(member);

                //put room code to member joinedRoomList
                member.getJoinedRoomCodes().add(room.getRoomCode());

                roomRepo.save(room);
                memberRepo.save(member);
                return new HashMap<>(Map.of("member" , member , "room" , room.getId() , "message", "success"));
            }
            else{
                if(!room.getRoomCode().equals(roomCode)) return new HashMap<>(Map.of("message", "Incorrect roomCode"));

                //put member to room
                room.getMembers().add(member);

                //put room code to member joinedRoomList
                member.getJoinedRoomCodes().add(room.getRoomCode());

                roomRepo.save(room);
                memberRepo.save(member);
                return new HashMap<>(Map.of("member" , member , "room" , room.getId() , "message", "success"));
            }
        }else{
            return new HashMap<>(Map.of("message" , "login again! Member not exist"));
        }
    }
}
