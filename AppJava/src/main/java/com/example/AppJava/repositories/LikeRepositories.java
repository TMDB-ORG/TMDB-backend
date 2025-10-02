package com.example.AppJava.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.AppJava.entities.LikeEntity;
import java.util.List;

@Repository
public interface LikeRepositories extends JpaRepository<LikeEntity, Long> {
    LikeEntity findByUserIdAndMovieId(Long userId, Long movieId);
    List<LikeEntity> findByUserId(Long userId);
    List<LikeEntity> findByMovieId(Long movieId);
    void deleteByUserIdAndMovieId(Long userId, Long movieId);
}
