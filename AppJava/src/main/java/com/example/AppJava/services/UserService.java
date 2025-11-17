package com.example.AppJava.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.AppJava.entities.UserEntity;
import com.example.AppJava.repositories.UserRepositories;
import com.example.AppJava.utils.JwtUtil;
@Service
public class UserService {

    @Autowired
    private UserRepositories userRepositories;



    public String createUser(UserEntity user) {
        if(user.getUsername() == null) {
            throw new IllegalArgumentException("Nome obrigatorio");
        }
        if(user.getEmail() == null) {
            throw new IllegalArgumentException("Email obrigatorio");
        }
        if(userRepositories.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }
        if(user.getPassword() == null) {
            throw new IllegalArgumentException("Senha é obrigatorio");
        }
        System.out.println("Dados no service: " + user);
        String hashedPasssword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hashedPasssword);
         String token = JwtUtil.generateToken(user.getId());
         System.out.println("Token gerado: " + token);
        UserEntity savedUser = userRepositories.save(user);
        if(savedUser != null){
            System.out.println("Usuario salvo com sucesso: " + savedUser);
            return "Usuario criado com sucesso";
        } else {
            throw new RuntimeException("Falha ao criar usuario");
        }
    }
    public String loginUser(String email, String password) {

        UserEntity user = userRepositories.findByEmail(email);

        if(user == null) {
            throw new IllegalArgumentException("Usuario nao encontrado");
        }
       
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    if (!encoder.matches(password, user.getPassword())) { 
        throw new IllegalArgumentException("Senha inválida");
    }
        String token = JwtUtil.generateToken(user.getId());
        return token;
    }
    public UserEntity getUserById(Long id) {
        return userRepositories.findById(id).orElse(null);
    }
}
