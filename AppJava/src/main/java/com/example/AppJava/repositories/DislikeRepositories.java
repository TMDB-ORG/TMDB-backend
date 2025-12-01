package com.example.AppJava.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.AppJava.entities.DislikeEntity;

@Repository
public interface DislikeRepositories extends JpaRepository<DislikeEntity, Long> {
    List<DislikeEntity> findByUserId(Long userId);
    List<DislikeEntity> findByMovieId(Long movieId);
    Number countByMovieId(Long movieId);
    DislikeEntity findByUser_IdAndMovie_Id(Long userId, Long movieId);
    void deleteByUser_IdAndMovie_Id(Long userId, Long movieId);
    @Query("""
        SELECT d.movie.id AS movieId, COUNT(d.id) AS total
        FROM DislikeEntity d
        GROUP BY d.movie.id
        ORDER BY total DESC
        LIMIT 3
    """)
    List<Object[]> findTop3MoviesWithMostDislikes();
}
