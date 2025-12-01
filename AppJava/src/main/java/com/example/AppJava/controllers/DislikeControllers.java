package com.example.AppJava.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.AppJava.repositories.MovieRepositories;
import com.example.AppJava.repositories.UserRepositories;
import com.example.AppJava.services.DislikeService;
import com.example.AppJava.utils.JwtUtil;

@RestController
@RequestMapping("/dislikes")
@CrossOrigin(origins = "*")
public class DislikeControllers {
    
    @Autowired
    private DislikeService dislikeService;

    @Autowired
    private UserRepositories userRepositories; 

    @Autowired
    private MovieRepositories movieRepositories;

    @PostMapping("/toggle")
public ResponseEntity<Map<String,Object>> toggleDislike(
        @RequestParam Long movieId,
        @CookieValue(value = "token", defaultValue = "") String token) {

    try {
        if (token.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        Number idUser = JwtUtil.validateToken(token);

        String newStatus = dislikeService.toggleDislike(idUser.longValue(), movieId);

        Long count = dislikeService.countDislikesByMovieId(movieId).longValue();

        return ResponseEntity.ok(
                Map.of(
                        "newStatus", newStatus,  
                        "count", count
                )
        );

    } catch (Exception e) {
        return ResponseEntity.status(500)
                .body(Map.of("error", "Internal Server Error: " + e.getMessage()));
    }
}

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getDislikeStatus(
            @RequestParam Long userId, 
            @RequestParam Long movieId) {
        try {
            String status = dislikeService.findByUserIdAndMovieId(userId, movieId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("ERRO em getDislikeStatus: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
    @GetMapping("/top3")
public ResponseEntity<List<Map<String, Object>>> getTop3MostDislikedMovies() {
    try {
        List<Map<String, Object>> top3 = dislikeService.getTop3MostDislikedMovies();
        return ResponseEntity.ok(top3);
    } catch (Exception e) {
        return ResponseEntity.status(500)
                .body(null);
    }
}
    @GetMapping("/count")
public ResponseEntity<Map<String, Object>> getDislikeCount(@RequestParam Long movieId) {
    try {
        Long count = dislikeService.countDislikesByMovieId(movieId).longValue();
        return ResponseEntity.ok(Map.of("count", count));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
    }
}
}