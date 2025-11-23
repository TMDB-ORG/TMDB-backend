package com.example.AppJava.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class JwtUtil {

    private static final String BASE_URL = "http://localhost:8081";

    public static String generateToken(Long userId) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        String url = BASE_URL + "/gerar-token?userId=" + userId;

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
                return "TokenBasedOn:" + response.body();
            }

            throw new RuntimeException("Erro ao gerar token. Código: " + response.statusCode());

        } catch (Exception e) {
            throw new RuntimeException("Falha na conexão com o servidor de tokens", e);
        }
    }

    public static Long validateToken(String token) {
        if (token == null || !token.startsWith("TokenBasedOn:")) {
            throw new RuntimeException("Token inválido");
        }

        String realToken = token.replaceFirst("TokenBasedOn:", "");

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/verificar-token"))
                .GET()
                .header("Authorization", "Bearer " + realToken)
                .timeout(Duration.ofSeconds(5))
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {

                String body = response.body();
                String[] parts = body.split(":");
                if (parts.length == 2) {
                    return Long.parseLong(parts[1].trim());
                } else {
                    throw new RuntimeException("Formato de resposta inesperado: " + body);
                }
            }

            throw new RuntimeException("Token inválido ou expirado. Código: " + response.statusCode());

        } catch (Exception e) {
            throw new RuntimeException("Falha na validação do token", e);
        }
    }
}
