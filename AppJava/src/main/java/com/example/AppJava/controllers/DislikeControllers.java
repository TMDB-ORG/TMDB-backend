package com.example.AppJava.controllers;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import com.example.AppJava.entities.DislikeEntity;
import com.example.AppJava.services.DislikeService;
import java.util.List;
import java.util.HashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
@RestController
@RequestMapping("/dislikes")
@CrossOrigin(origins = "*")
public class DislikeControllers {
    @Autowired
    private DislikeService dislikeService;

    @PostMapping("/dislikeMovie")
    public ResponseEntity<Map<String,Object>> dislikeMovie(@RequestBody DislikeEntity dislike){
        try {
            System.out.println("Dados no controller: " + dislike);
            Map<String,Object> response = new HashMap<>();
            String message = dislikeService.saveUnlike(dislike);
            response.put("message", message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getDislikeStatus(@RequestParam Long userId, @RequestParam Long movieId) {
        try {
            String status = dislikeService.findByUserIdAndMovieId(userId, movieId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
}