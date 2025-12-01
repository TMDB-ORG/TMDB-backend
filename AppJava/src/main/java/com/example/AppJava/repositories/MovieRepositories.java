package com.example.AppJava.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;  

import com.example.AppJava.entities.MovieEntity;

@Repository
public interface MovieRepositories extends JpaRepository<MovieEntity, Long> {
    MovieEntity findById(long id);
}