package com.example.skyNews.dto.response;

import com.example.skyNews.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleViewedInfoResponse {
    private Article article;
    private Integer isUserSaved;
}
