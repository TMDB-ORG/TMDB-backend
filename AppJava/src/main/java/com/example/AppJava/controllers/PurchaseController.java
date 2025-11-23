package com.example.AppJava.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api")
public class PurchaseController {

    @PostMapping("/purchase")
    public ResponseEntity<Map<String, Object>> createPurchase(@RequestBody Map<String, Object> payload) {
        Long movieId = Long.valueOf(payload.get("movieId").toString());
        Double price = Double.valueOf(payload.get("price").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());

        String transactionId = "TXN" + System.currentTimeMillis();
        
        // Lógica para processar a compra (salvar no banco de dados, etc.)
        // Aqui você pode chamar um serviço para lidar com a lógica de compra

    

        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", transactionId);
        response.put("message", "Compra criada com sucesso!");

        return ResponseEntity.ok(response);
    }
}
