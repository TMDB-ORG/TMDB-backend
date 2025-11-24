package com.example.AppJava.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.AppJava.entities.DislikeEntity;

@Repository
public interface DislikeRepositories extends JpaRepository<DislikeEntity, Long> {
    DislikeEntity findByUserIdAndMovieId(Long userId, Long movieId);
    List<DislikeEntity> findByUserId(Long userId);
    List<DislikeEntity> findByMovieId(Long movieId);
    Number countByMovieId(Long movieId);
    void deleteByUserIdAndMovieId(Long userId, Long movieId);
    
}
