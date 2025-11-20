package com.example.AppJava.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.AppJava.entities.DislikeEntity;
import java.util.List;

@Repository
public interface DislikeRepositories extends JpaRepository<DislikeEntity, Long> {
    DislikeEntity findByUserIdAndMovieId(Long userId, Long movieId);
    List<DislikeEntity> findByUserId(Long userId);
    List<DislikeEntity> findByMovieId(Long movieId);
    void deleteByUserIdAndMovieId(Long userId, Long movieId);
}
