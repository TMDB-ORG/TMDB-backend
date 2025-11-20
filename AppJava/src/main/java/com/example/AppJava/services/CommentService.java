package com.example.AppJava.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
