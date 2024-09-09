package org.example.translator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.translator.DTO.ApiRequest;
import org.example.translator.DTO.ApiResponse;
import org.example.translator.Service.TranslationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;



@SpringBootTest
class TranslatorApplicationTests {

    @Autowired
    private TranslationService translationService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void contextLoads() throws JsonProcessingException {
        String[] testWord = {"Hello"};
        TranslationService TranslationService = Mockito.mock(TranslationService.class);
        ApiRequest apiRequest = new ApiRequest(testWord, "en", "ru");

        TranslationService.translateWord(apiRequest);
        Mockito.verify(TranslationService).translateWord(apiRequest);
    }

    @Test
    void contextLoads2() throws JsonProcessingException {
        ApiRequest apiRequest = new ApiRequest(new String[]{"Hello"}, "en", "ru");

        ApiResponse mocResponse = new ApiResponse();
        Mockito.when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(ApiResponse.class)))
                .thenReturn(mocResponse);

        ApiResponse apiResponse = translationService.translateWord(apiRequest);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(apiRequest);

        Mockito.verify(restTemplate).postForObject(
                eq("https://translate.api.cloud.yandex.net/translate/v2/translate"),
                Mockito.argThat((HttpEntity<String> requestEntity) -> {
                    boolean testJson = requestEntity.getBody().equals(expectedJson);
                    HttpHeaders headers = requestEntity.getHeaders();
                    boolean contentType = headers.getContentType().equals(MediaType.APPLICATION_JSON);
                    boolean apiKeyTest = headers.getFirst("Authorization")
                            .equals("Api-Key " + "AQVNzzqiL4stEfsz2wfijhEBHkSqeU9Jy9czpPOH");
                    return testJson && contentType && apiKeyTest;
                }),
                eq(ApiResponse.class)
        );

        assertEquals(mocResponse, apiResponse);
    }
}
