package com.example.skyNews.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetArticleCategoryTopicPageRequest {
    private String category;
    private String topic;
    private int page;
    private int pageSize;
}
