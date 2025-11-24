package com.example.AppJava.services;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AppJava.DTO.CommentDTO;
import com.example.AppJava.entities.CommentEntity;
import com.example.AppJava.repositories.CommentRepositories;
@Service
public class CommentService {
    @Autowired
    private CommentRepositories commentRepositories;

    public String saveComment(CommentEntity comment) {
        // Falta a logica para salvar o comentário no banco de dados
        CommentEntity savedComment = commentRepositories.save(comment);
        if(savedComment != null){
            System.out.println("Comentario salvo com sucesso: " + savedComment);
            return "Comentario  criado com sucesso";
            } else {
                throw new RuntimeException("Falha ao criar comentario");
            }
    }
    public List<CommentDTO> getCommentsByMovieId(Long movieId) {
        List<CommentEntity> comments = commentRepositories.findByMovieId(movieId);
    
    return comments.stream()
            .map(CommentDTO::new)
            .collect(Collectors.toList());
    }
}
