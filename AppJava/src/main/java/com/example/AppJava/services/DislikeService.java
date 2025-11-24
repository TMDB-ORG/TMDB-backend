package com.example.AppJava.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AppJava.entities.DislikeEntity;
import com.example.AppJava.entities.MovieEntity;
import com.example.AppJava.entities.UserEntity;
import com.example.AppJava.repositories.DislikeRepositories;

@Service
public class DislikeService {
    @Autowired
    private DislikeRepositories dislikeRepository;
    @Autowired
    private UserService userService;
    @Autowired

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
    public DislikeEntity findById(Long id) {
        return dislikeRepository.findById(id).orElse(null);
    }
    public String removeDislike(Long userId, Long movieId) {
        try {
            dislikeRepository.deleteByUserIdAndMovieId(userId, movieId);
            return "Dislike removido com sucesso";
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover dislike");
        }
    }

    public String toggleDislike(Long userId, Long movieId) {


    DislikeEntity existing = dislikeRepository.findByUserIdAndMovieId(userId, movieId);

    if (existing != null) {
     
        dislikeRepository.delete(existing);
        return "Dislike removido";
    }

   
    DislikeEntity d = new DislikeEntity();
    UserEntity user = userService.getUserById(userId);
    MovieEntity movie = new MovieEntity();
    movie.setId(movieId);
    d.setUser(user);
    d.setMovie(movie);

    dislikeRepository.save(d);
    return "Dislike adicionado";
}

    public List<DislikeEntity> findByMovieId(Long movieId) {
        return dislikeRepository.findByMovieId(movieId);
    }

    public void deleteByUserIdAndMovieId(Long userId, Long movieId) {
        dislikeRepository.deleteByUserIdAndMovieId(userId, movieId);
    }
    public void deleteById(Long id) {
        dislikeRepository.deleteById(id);
    }
    public Number countDislikesByMovieId(Long movieId) {
        return dislikeRepository.countByMovieId(movieId);
    }
}