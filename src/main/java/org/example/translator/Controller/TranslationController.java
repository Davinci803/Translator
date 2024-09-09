package org.example.translator.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.translator.DTO.ApiRequest;
import org.example.translator.DTO.ApiResponse;
import org.example.translator.Service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TranslationController {
    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @GetMapping("/translate")
    public ResponseEntity<String> translate(@RequestBody ApiRequest apiRequest) {

        try {

            ApiResponse apiResponse = translationService.translateWord(apiRequest);
            return ResponseEntity.ok(apiResponse.getTranslations().get(0).getText());

        } catch (JsonProcessingException e) {

            return ResponseEntity.status(500).body("Ошибка в JSON: " + e.getMessage());

        }

    }
}
