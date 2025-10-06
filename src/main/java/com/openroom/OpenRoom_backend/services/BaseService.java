package com.openroom.OpenRoom_backend.services;

import com.openroom.OpenRoom_backend.models.Member;
import com.openroom.OpenRoom_backend.models.MembersInfoDTO;
import com.openroom.OpenRoom_backend.models.Room;
import com.openroom.OpenRoom_backend.models.RoomDto;
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
        roomRepo.save(room);

        member.getJoinedRoomCodes().add(room.getRoomCode());
        memberRepo.save(member);

        return new HashMap<>(Map.of(
                "message" , "success"
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
                room.getMembers().add(member);

                member.getJoinedRoomCodes().add(room.getRoomCode());

                roomRepo.save(room);
                memberRepo.save(member);
                return new HashMap<>(Map.of("member" , member , "room" , room.getId() , "message", "success"));
            }
            else{
                if(!room.getRoomCode().equals(roomCode)) return new HashMap<>(Map.of("message", "Incorrect roomCode"));

                room.getMembers().add(member);

                member.getJoinedRoomCodes().add(room.getRoomCode());

                roomRepo.save(room);
                memberRepo.save(member);
                return new HashMap<>(Map.of("member" , member , "room" , room.getId() , "message", "success"));
            }
        }else{
            return new HashMap<>(Map.of("message" , "login again! Member not exist"));
        }
    }

    public Map<String, Object> getRooms(String email) {
        List<Room> allRooms = roomRepo.findAll();
        Member member = memberService.getMemberByEmail(email);
        if (member == null) {
            return Map.of("message", "Member not found, login again!");
        }

        List<String> joinedRoomCodes = member.getJoinedRoomCodes();

        // Map Rooms to RoomDto
        List<RoomDto> myRooms = allRooms.stream()
                .filter(room -> joinedRoomCodes.contains(room.getRoomCode()))
                .map(room -> new RoomDto(
                        room.getId(),
                        room.getName(),
                        room.getMemberLimit(),
                        room.isPrivacy(),
                        room.isAi(),
                        room.getRoomCode(),
                        room.getAdminId(),
                        room.getMembers().size()
                ))
                .toList();

        List<RoomDto> availableRooms = allRooms.stream()
                .filter(room -> !joinedRoomCodes.contains(room.getRoomCode()))
                .map(room -> new RoomDto(
                        room.getId(),
                        room.getName(),
                        room.getMemberLimit(),
                        room.isPrivacy(),
                        room.isAi(),
                        room.getRoomCode(),
                        room.getAdminId(),
                        room.getMembers().size()
                ))
                .toList();

        return Map.of(
                "myRooms", myRooms,
                "availableRooms", availableRooms
        );
    }

    public Map<String, Object> checkUserValidity(int roomId, String email) {
        Room room = roomRepo.findById(roomId).orElse(null);
        Member member = memberRepo.findByEmail(email);
        if(room == null || member == null) return new HashMap<>(Map.of("message" , "Not valid"));

        String roomCode = room.getRoomCode();
        for(String code : member.getJoinedRoomCodes()){
            if(roomCode.equals(code)) return new HashMap<>(Map.of("message", "valid", "ai", room.isAi(), "roomName", room.getName()));
        }
        return new HashMap<>(Map.of("message", "Not valid"));
    }

    public Map<String,Object> roomMembersInfo(int roomId) {
        Room room = roomRepo.findById(roomId).orElse(null);
        Member admin = memberRepo.findById(room.getAdminId()).orElse(null);
        MembersInfoDTO adminDto = new MembersInfoDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getPicture());
        if(room == null) return null;
        List<Member> members = room.getMembers();
        List<MembersInfoDTO> membersInfoDto = new LinkedList<>();
        for(Member m : members){
            membersInfoDto.add(new MembersInfoDTO(m.getId(), m.getName(), m.getEmail(), m.getPicture()));
        }
        return new HashMap<>(Map.of(
                "membersInfo" , membersInfoDto,
                "roomName", room.getName(),
                "admin" , adminDto
        ));
    }
}
