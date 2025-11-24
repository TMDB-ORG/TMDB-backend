package com.example.AppJava.controllers;

import java.util.HashMap;
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

import com.example.AppJava.entities.DislikeEntity;
import com.example.AppJava.entities.MovieEntity;
import com.example.AppJava.entities.UserEntity;
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

    @PostMapping("/dislikeMovie")
    public ResponseEntity<Map<String,Object>> dislikeMovie(
            @RequestParam Long movieId, 
            @CookieValue(value = "token", defaultValue = "") String token) {
        try {
            System.out.println("Dislike request - movieId: " + movieId + ", token: " + token);
            
            if (token.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: No token provided"));
            }
            if (movieId == null) {
                return ResponseEntity.status(400).body(Map.of("error", "movieId is required"));
            }

            Number idUser = JwtUtil.validateToken(token);
            UserEntity user = userRepositories.findById(idUser.longValue());
            
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

            MovieEntity movie = movieRepositories.findById(movieId).orElse(null);
            if (movie == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Movie not found"));
            }

            DislikeEntity dislike = new DislikeEntity();
            dislike.setUser(user);
            dislike.setMovie(movie);

            String message = dislikeService.saveUnlike(dislike);
            Map<String,Object> response = new HashMap<>();
            response.put("message", message);
          
            Long count = dislikeService.countDislikesByMovieId(movieId).longValue();
            response.put("count", count);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("ERRO em dislikeMovie: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error: " + e.getMessage()));
        }
    }
    @PostMapping("/toggle")
public ResponseEntity<Map<String,Object>> toggleDislike(
        @RequestParam Long movieId,
        @CookieValue(value = "token", defaultValue = "") String token) {
    try {

        if (token.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        Number idUser = JwtUtil.validateToken(token);

        // Chama o toggle
        String message = dislikeService.toggleDislike(idUser.longValue(), movieId);

        // Atualiza contagem
        Long count = dislikeService.countDislikesByMovieId(movieId).longValue();

        return ResponseEntity.ok(
                Map.of(
                        "message", message,
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

    @GetMapping("/count")
public ResponseEntity<Map<String, Object>> getDislikeCount(@RequestParam Long movieId) {
    try {
        Long count = dislikeService.countDislikesByMovieId(movieId).longValue();
        return ResponseEntity.ok(Map.of("count", count));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
    }
}

    @PostMapping("/removeDislike")
    public ResponseEntity<Map<String, Object>> removeDislike(
            @RequestParam Long movieId,
            @CookieValue(value = "token", defaultValue = "") String token) {
        try {
            System.out.println("Remove dislike - movieId: " + movieId + ", token: " + token);
            
            if (token.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            Number idUser = JwtUtil.validateToken(token);
            String result = dislikeService.removeDislike(idUser.longValue(), movieId);
            
         
            Long count = dislikeService.countDislikesByMovieId(movieId).longValue();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", result);
            response.put("count", count);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("ERRO em removeDislike: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error: " + e.getMessage()));
        }
    }
}