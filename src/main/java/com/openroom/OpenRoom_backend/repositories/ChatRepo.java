package com.openroom.OpenRoom_backend.repositories;

import com.openroom.OpenRoom_backend.models.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<ChatMessage,Integer> {
    Page<ChatMessage> findByRoom_Id(Integer roomId, Pageable pageable);
}
