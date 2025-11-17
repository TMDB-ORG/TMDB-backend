package com.example.AppJava.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Comment_tb")
public class CommentEntity {
    @Id
    @GeneratedValue( strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String contentString;

    @Column(name = "userId", nullable = false)
    private Long userId;
    
}
