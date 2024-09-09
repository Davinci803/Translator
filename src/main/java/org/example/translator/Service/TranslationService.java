package org.example.translator.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.translator.DTO.*;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class TranslationService {
    ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.key}")
    private String apiKey;

     public TranslationService(RestTemplate restTemplate){
         this.restTemplate = restTemplate;
     }

     public ApiResponse translateWord(ApiRequest apiRequest) throws JsonProcessingException {

         String json = objectMapper.writeValueAsString(apiRequest);

         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_JSON);
         headers.set("Authorization", "Api-Key " + apiKey);

         HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

         try{
             ApiResponse apiResponse = restTemplate.postForObject(apiUrl, requestEntity, ApiResponse.class);
             return apiResponse;

         } catch (HttpClientErrorException e){
             System.out.println("Error: " + e.getMessage());
             throw e;
         }
     }
}
