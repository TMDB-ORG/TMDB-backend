package com.example.AppJava.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.AppJava.entities.CommentEntity;
@Repository
public interface  CommentRepositories extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByUserId(Long userId);
}
