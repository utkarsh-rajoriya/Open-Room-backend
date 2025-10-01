package com.openroom.OpenRoom_backend.repositories;

import com.openroom.OpenRoom_backend.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepo extends JpaRepository<Member,Integer> {
    Member findByEmail(String email);
}
