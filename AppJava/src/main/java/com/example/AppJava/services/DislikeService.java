package com.example.AppJava.services;
import com.example.AppJava.entities.DislikeEntity;
import com.example.AppJava.repositories.DislikeRepositories;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DislikeService {
    @Autowired
    private DislikeRepositories dislikeRepository;

    public String findByUserIdAndMovieId(Long userId, Long movieId) {
        if (dislikeRepository.findByUserIdAndMovieId(userId, movieId) != null) {
            return "Unliked";
        } else {
            return "Not Unliked";
        }

    }
    public String saveUnlike(DislikeEntity unlike) {
        if(unlike.getUser() == null ) {
            throw new IllegalArgumentException("user não esta logado");
        }
        if(unlike.getMovie() == null ) {
            throw new IllegalArgumentException("movie é obrigatorio");
        }
        if (dislikeRepository.save(unlike) != null) {
            return "Unlike salvo com sucesso";
        } else {
            throw new RuntimeException("Erro ao salvar unlike");
        }
    }
    public List<DislikeEntity> findByUserId(Long userId) {
        return dislikeRepository.findByUserId(userId);
    }

    public List<DislikeEntity> findByMovieId(Long movieId) {
        return dislikeRepository.findByMovieId(movieId);
    }

    public void deleteByUserIdAndMovieId(Long userId, Long movieId) {
        dislikeRepository.deleteByUserIdAndMovieId(userId, movieId);
    }
}