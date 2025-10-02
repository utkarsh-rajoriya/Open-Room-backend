package com.openroom.OpenRoom_backend.controllers;

import com.openroom.OpenRoom_backend.models.Member;
import com.openroom.OpenRoom_backend.services.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class UserController {

    public MemberService memberService;

    public UserController(MemberService memberService) {
        this.memberService = memberService;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @GetMapping("test")
    public Map<String , String> testing(){
    return new HashMap<>(Map.of("testing" , "Testing app"));
    }

    @GetMapping("greet")
    public Map<String , String> greet(){
    return new HashMap<>(Map.of("greet" , "Heloo! World"));
    }

    @GetMapping("user-info")
    public Map<String , Object> userInfo(@AuthenticationPrincipal OAuth2User user){
        if(user == null){
            return Map.of("error" , "Unauthorized");
        }
        Member mbr = memberService.getMemberByEmail((String)user.getAttributes().get("email"));
        try {
            if(mbr == null) {
                Member member = new Member();
                member.setName((String) user.getAttributes().get("name"));
                member.setEmail((String) user.getAttributes().get("email"));
                member.setClientChatId(generateRandomString(8));
                if(user.getAttributes().get("picture") != null){
                member.setPicture((String) user.getAttributes().get("picture"));
                }else{
                member.setPicture((String) user.getAttributes().get("avatar_url"));
                }
                mbr = memberService.registerMember(member);
                System.out.println("member saved : " + mbr);
            }
            else if(user.getAttributes().get("picture") != null){
                if(!user.getAttributes().get("picture").equals(mbr.getPicture())){
                    mbr.setPicture((String)user.getAttributes().get("picture"));
                    memberService.registerMember(mbr);
                }
            }else if(user.getAttributes().get("avatar_url") != null) {
                if (!user.getAttributes().get("avatar_url").equals(mbr.getPicture())) {
                    mbr.setPicture((String) user.getAttributes().get("avatar_url"));
                    memberService.registerMember(mbr);
                }
            }

            return new HashMap<>(Map.of(
                    "userInfo", user.getAttributes(),
                    "clientChatId", mbr.getClientChatId()
            ));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new HashMap<>(Map.of("message", "fail"));
        }
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "Logged out successfully";
    }
}
