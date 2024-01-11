package com.example.skyNews.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateArticleRequest {
    private String title;
    private String mainImage;
    private String content;
    private String location;
    private String date;
    private Long authorId;
    private int isHot;
    private String category;
    private String topic;
    private String tags;
}
