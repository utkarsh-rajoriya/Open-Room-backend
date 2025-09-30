package com.openroom.OpenRoom_backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class UserController {

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
        System.out.println("running");
        if(user == null){
            return Map.of("error" , "Unauthorized");
        }

        try {
            return user.getAttributes();
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
