package com.example.skyNews.dto.response;

import com.example.skyNews.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetListArticlesResponse {
    private List<Article> listArticle;
    private int numArticle;
}
