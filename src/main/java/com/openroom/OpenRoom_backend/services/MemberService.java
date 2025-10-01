package com.openroom.OpenRoom_backend.services;

import com.openroom.OpenRoom_backend.models.Member;
import com.openroom.OpenRoom_backend.repositories.MemberRepo;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public MemberRepo memberRepo;

    public MemberService(MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }

    public void  registerMember(Member member){
        System.out.println("member saved : " +  member.toString());
        memberRepo.save(member);
    }

    public Member getMemberByEmail(String email) {
        return memberRepo.findByEmail(email);
    }
}
