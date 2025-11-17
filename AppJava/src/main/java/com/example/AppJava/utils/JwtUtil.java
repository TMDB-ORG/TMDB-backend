package com.example.AppJava.utils;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Key;
import java.time.Duration;

import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static final Key secretKey = Keys.hmacShaKeyFor("minhaChaveSuperSecretaDeNoMinimo32Chars".getBytes());
    public static String generateToken(Long userId) {

    HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    String url = "http://localhost:8081/gerar-token?userId=" + userId;

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .timeout(Duration.ofSeconds(5))
            .build();

    try {
        HttpResponse<String> response = client.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() == 200) {
            String body = response.body();
            return "TokenBasedOn:" + body;
        }

        throw new RuntimeException("Erro ao gerar token. Código: " + response.statusCode());

    } catch (Exception e) {
        throw new RuntimeException("Falha na conexão com o servidor de tokens", e);
    }
}

    
}
