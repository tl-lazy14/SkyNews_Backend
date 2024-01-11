package com.example.skyNews.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditArticleRequest {
    private String title;
    private String mainImage;
    private String content;
    private String location;
    private String date;
    private int isHot;
    private String category;
    private String topic;
    private String tags;
}
