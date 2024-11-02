package com.sachit.tedtalk.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TED_TALK")
public class TedTalk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TED_TALK_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "DATE")
    private String date;

    @Column(name = "VIEWS")
    private Long views;

    @Column(name = "LIKES")
    private Long likes;

    @Column(name = "LINK")
    private String link;


}
