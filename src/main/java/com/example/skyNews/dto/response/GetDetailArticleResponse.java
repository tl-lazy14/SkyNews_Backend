package com.example.skyNews.dto.response;

import com.example.skyNews.entity.Article;
import com.example.skyNews.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetDetailArticleResponse {
    private Article article;
    private List<String> tags;
}
