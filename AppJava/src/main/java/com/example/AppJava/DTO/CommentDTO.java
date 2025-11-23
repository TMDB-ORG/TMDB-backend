package com.example.AppJava.DTO;
import java.time.LocalDateTime;

import com.example.AppJava.entities.CommentEntity;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private String username;
    private Long movieId;
    private LocalDateTime createdAt;

    public CommentDTO(CommentEntity comment) {
        this.id = comment.getId();
        this.content = comment.getContentString();
        this.username = comment.getUser().getUsername();
        this.movieId = comment.getMovie().getId();
        this.createdAt = comment.getCreatedAt();
    }
}