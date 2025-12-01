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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.AppJava.DTO.CommentDTO;
import com.example.AppJava.entities.CommentEntity;
import com.example.AppJava.entities.MovieEntity;
import com.example.AppJava.entities.UserEntity;
import com.example.AppJava.repositories.MovieRepositories;
import com.example.AppJava.repositories.UserRepositories;
import com.example.AppJava.services.CommentService;
import com.example.AppJava.utils.JwtUtil;
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class CommentControllers {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserRepositories userRepositories;
    @Autowired 
    private MovieRepositories movieRepositories;


    
    @PostMapping("/saveComment")
    public ResponseEntity<Map<String,Object>> SaveComments(
            @RequestBody CommentEntity comment, 
            @RequestParam(required = false) Long movieId,
            @CookieValue(value = "token", defaultValue = "") String token) {

        try {
            System.out.println("Dados no controller: " + comment);
            
            if (movieId == null) {
                return ResponseEntity.status(400).body(Map.of("error", "movieId is required"));
            }
            if (token.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: No token provided"));
            }
            MovieEntity movie = movieRepositories.findById(movieId).orElse(null);

            if(movie == null) {
                movie = new MovieEntity();
                movie.setId(movieId);
                movieRepositories.save(movie);

            }
    
            System.out.println("Filme encontrado: " + movie);
            comment.setMovie(movie);
            Map<String,Object> response = new HashMap<>();
            Number userId = JwtUtil.validateToken(token);
            System.out.println("User ID from token: " + userId);

            if(userId != null) {
               
                UserEntity user = userRepositories.findById(userId.longValue());
                if (user == null) {
                    System.out.println("Usuário não encontrado para ID: " + userId);
                    return ResponseEntity.status(401).body(Map.of("error", "Usuário não encontrado"));
                }
                
                comment.setUser(user);
                String message = commentService.saveComment(comment);
                response.put("message", message);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }
  
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
    
    @GetMapping("/getComments")
    public ResponseEntity<Map<String,Object>> GetComments(@RequestParam Long movieId) {
        try {
            Map<String,Object> response = new HashMap<>();
            List<CommentDTO> comments = commentService.getCommentsByMovieId(movieId);
            System.out.println("Comments retrieved: " + comments.size());
            System.out.println("Comments data: " + comments.get(0));
             response.put("comments", comments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
}