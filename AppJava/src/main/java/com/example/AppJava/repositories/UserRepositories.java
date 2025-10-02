package com.example.AppJava.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.AppJava.entities.UserEntity;
@Repository
public interface UserRepositories extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findById(long id);
}
