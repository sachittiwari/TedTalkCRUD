package com.sachit.tedtalk.model;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TedTalkResponseDTO {

    private Long id;
    private String title;
    private String author;
    private String date;
    private Long views;
    private Long likes;
    private String link;
}
