package com.example.AppJava.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table
@Entity
public class MovieEntity {
    @Id
    private Long  Id;

    @Column(name = "Title", nullable = true)
    private String title;


}
