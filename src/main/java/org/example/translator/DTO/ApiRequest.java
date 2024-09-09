package org.example.translator.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequest {
    private String[] texts;
    private String source_language_code;
    private String target_language_code;
}
