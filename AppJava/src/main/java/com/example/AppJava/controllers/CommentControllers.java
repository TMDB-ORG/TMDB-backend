package com.example.AppJava.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AppJava.entities.CommentEntity;
import com.example.AppJava.services.CommentService;
@RestController
@RequestMapping("/users")

@CrossOrigin(origins = "*")
public class CommentControllers {
    @Autowired
    private CommentEntity commentEntity;
    private CommentService commentService;
    public ResponseEntity<Map<String,Object>> getComments(@RequestBody CommentEntity comment){
        try {
            System.out.println("Dados no controller: " + comment);
            Map<String,Object> response = new HashMap<>();
            String message = commentService.saveComment(comment);
            response.put("message", message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
}
