package com.example.AppJava.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import com.example.AppJava.entities.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.AppJava.entities.MovieEntity;

@Data
@Entity
@Table(name = "Comment_tb")
public class CommentEntity {

    
    @Id
    @GeneratedValue( strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String contentString;

     @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;
}
