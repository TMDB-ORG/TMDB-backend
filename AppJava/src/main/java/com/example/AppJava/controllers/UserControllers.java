package com.example.AppJava.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AppJava.entities.UserEntity;
import com.example.AppJava.services.UserService;    
import com.example.AppJava.utils.JwtUtil;
@RestController

@RequestMapping("/users")

@CrossOrigin(origins = "*")
public class UserControllers {

    @Autowired
    private UserService userService;

    
    @GetMapping("/getUser")
    public ResponseEntity<Map<String,Object>> getUser(@CookieValue(value = "token", defaultValue = "") String token) {
        Long id = JwtUtil.validateToken(token);
        UserEntity user = userService.getUserById(id);
        if(user != null) {
            return ResponseEntity.ok(Map.of("user", user));
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserEntity credentials) {
        String email = credentials.getEmail();
        String password = credentials.getPassword();
        try {
            String token = userService.loginUser(email, password);
            System.out.println("Generated Token: " + token);
    ResponseCookie cookie = ResponseCookie.from("token", token)
        .httpOnly(false)      
        .secure(false)       
        .path("/")          
        .maxAge(3600)        
        .sameSite("Lax")   
        .build();

return ResponseEntity.ok()
        .header("Set-Cookie", cookie.toString())
        .body(Map.of("message", "Login realizado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserEntity user) {
        try {
            System.out.println("Dados no controller: " + user);
            String response = userService.createUser(user);
            return ResponseEntity.ok(Map.of("message", response));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
    
}