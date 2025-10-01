package com.openroom.OpenRoom_backend.repositories;

import com.openroom.OpenRoom_backend.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepo extends JpaRepository<Room,Integer> {
    Room findByName(String name);
}
