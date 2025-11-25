package com.example.AppJava.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AppJava.entities.DislikeEntity;
import com.example.AppJava.entities.MovieEntity;
import com.example.AppJava.entities.UserEntity;
import com.example.AppJava.repositories.DislikeRepositories;
import com.example.AppJava.repositories.MovieRepositories;
import com.example.AppJava.repositories.UserRepositories;

@Service
public class DislikeService {
    @Autowired
    private DislikeRepositories dislikeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MovieRepositories  movieRepositories;
    @Autowired
    private UserRepositories userRepositories;
 


 public String findByUserIdAndMovieId(Long userId, Long movieId) {
            if (dislikeRepository.findByUser_IdAndMovie_Id(userId, movieId) != null) {
                return "DISLIKED";
            } else {
                return "NONE";
            }

    }
    public String saveUnlike(DislikeEntity dislike) {
        if(dislike.getUser() == null ) {
            throw new IllegalArgumentException("user não esta logado");
        }
        if(dislike.getMovie() == null ) {
            throw new IllegalArgumentException("movie é obrigatorio");
        }
        if (dislikeRepository.save(dislike) != null) {
            return "Unlike salvo com sucesso";
        } else {
            throw new RuntimeException("Erro ao salvar unlike");
        }
    }
    public DislikeEntity findById(Long id) {
        return dislikeRepository.findById(id).orElse(null);
    }
    public String removeDislike(Long userId, Long movieId) {
        try {
            dislikeRepository.deleteByUser_IdAndMovie_Id(userId, movieId);
            return "Dislike removido com sucesso";
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover dislike");
        }
    }

    public String toggleDislike(Long userId, Long movieId) {

    UserEntity user = userRepositories.findById(userId).orElse(null);
    MovieEntity movie = movieRepositories.findById(movieId).orElse(null);

    if (user == null || movie == null) {
        return "NONE";
    }

    DislikeEntity existing = dislikeRepository.findByUser_IdAndMovie_Id(userId, movieId);

    if (existing != null) {
        dislikeRepository.delete(existing);
        return "NONE"; 
    }

    DislikeEntity d = new DislikeEntity();
    d.setUser(user);
    d.setMovie(movie);

    dislikeRepository.save(d);
    return "DISLIKED"; 
}


    public List<DislikeEntity> findByMovieId(Long movieId) {
        return dislikeRepository.findByMovieId(movieId);
    }

    public Number countDislikesByMovieId(Long movieId) {
        return dislikeRepository.countByMovieId(movieId);
    }
}