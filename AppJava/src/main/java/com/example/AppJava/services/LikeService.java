package com.example.AppJava.services;
import com.example.AppJava.entities.LikeEntity;
import com.example.AppJava.repositories.LikeRepositories;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikeRepositories likeRepository;

    public LikeEntity findByUserIdAndMovieId(Long userId, Long movieId) {
        return likeRepository.findByUserIdAndMovieId(userId, movieId);
    }
    public LikeEntity saveLike(LikeEntity like) {
        if(like.getUserId() == null ) {
            throw new IllegalArgumentException("user não esta logado");
        }
        if(like.getMovieId() == null ) {
            throw new IllegalArgumentException("movieId é obrigatorio");
        }
        return likeRepository.save(like);
    }
    public List<LikeEntity> findByUserId(Long userId) {
        return likeRepository.findByUserId(userId);
    }

    public List<LikeEntity> findByMovieId(Long movieId) {
        return likeRepository.findByMovieId(movieId);
    }

    public void deleteByUserIdAndMovieId(Long userId, Long movieId) {
        likeRepository.deleteByUserIdAndMovieId(userId, movieId);
    }
}