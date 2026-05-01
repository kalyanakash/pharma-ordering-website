package com.pharma.backend.controllers;

import com.pharma.backend.payload.request.ChatbotRequest;
import com.pharma.backend.payload.response.ChatbotResponse;
import com.pharma.backend.service.AiChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"}, maxAge = 3600)
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatbotController {

    private final AiChatbotService aiChatbotService;

    @PostMapping
    public ResponseEntity<ChatbotResponse> askQuestion(@Valid @RequestBody ChatbotRequest request) {
        try {
            String answer = aiChatbotService.askQuestion(request.getMessage());
            return ResponseEntity.ok(new ChatbotResponse(answer));
        } catch (Exception e) {
            System.err.println("[CHATBOT ERROR] " + e.getClass().getSimpleName() + ": " + e.getMessage());
            return ResponseEntity.ok(new ChatbotResponse("I'm sorry, I'm having trouble connecting right now. Please try again later."));
        }
    }
}
