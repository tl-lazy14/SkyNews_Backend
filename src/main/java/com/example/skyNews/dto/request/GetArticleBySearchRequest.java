package com.example.skyNews.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetArticleBySearchRequest {
    private String category;
    private String keyword;
    private int page;
    private int pageSize;
}
