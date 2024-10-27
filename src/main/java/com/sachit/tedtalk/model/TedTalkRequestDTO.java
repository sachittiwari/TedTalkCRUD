package com.sachit.tedtalk.model;

import lombok.Data;

@Data
public class TedTalkRequestDTO {

    private String title;
    private String author;
    private String date;
    private Long views;
    private Long likes;
    private String link;


}
