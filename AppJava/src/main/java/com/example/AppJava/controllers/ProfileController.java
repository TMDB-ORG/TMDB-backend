package com.example.AppJava.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AppJava.entities.CommentEntity;
import com.example.AppJava.entities.DislikeEntity;
import com.example.AppJava.entities.UserEntity;
import com.example.AppJava.repositories.CommentRepositories;
import com.example.AppJava.repositories.DislikeRepositories;
import com.example.AppJava.repositories.UserRepositories;
import com.example.AppJava.utils.JwtUtil;
@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private UserRepositories userRepo;

    @Autowired
    private CommentRepositories commentRepo;

    @Autowired
    private DislikeRepositories dislikeRepo;

    @GetMapping("/user")
    public Map<String, Object> getProfile(@CookieValue(value = "token", defaultValue = "") String token) throws Exception {

        Long userId = JwtUtil.validateToken(token).longValue();
        UserEntity user = userRepo.findById(userId).orElse(null);

        if (user == null) {
            return Map.of("error", "Usuário não encontrado");
        }

        List<CommentEntity> comments = commentRepo.findByUserId(userId);

  
        List<DislikeEntity> dislikes = dislikeRepo.findByUserId(userId);


        List<Map<String, Object>> mappedComments = comments.stream()
                .map(c -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", c.getId());
                    m.put("content", c.getContentString());
                    m.put("movieId", c.getMovie().getId());
                    m.put("createdAt", c.getCreatedAt());
                    return m;
                })
                .toList();

        List<Map<String, Object>> mappedDislikes = dislikes.stream()
                .map(d -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("movieId", d.getMovie().getId());
                    return m;
                })
                .toList();

        return Map.of(
                "user", user,
                "comments", mappedComments,
                "dislikes", mappedDislikes
        );
    }
}
