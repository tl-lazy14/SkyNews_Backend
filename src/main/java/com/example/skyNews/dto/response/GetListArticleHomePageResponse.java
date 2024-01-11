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
public class GetListArticleHomePageResponse {
    private List<Article> listArticleMix;
    private List<Article> articleBusiness;
    private List<Article> articleRealEstate;
    private List<Article> articleSport;
    private List<Article> articleEntertainment;
    private List<Article> articleHealth;
    private List<Article> articleLife;
    private List<Article> articleEducation;
    private List<Article> articleScience;
    private List<Article> articleDigitizing;
    private List<Article> articleNews;
    private List<Article> articleWorld;
    private List<Article> articleLaw;
    private List<Article> articleTourism;
    private List<Article> articleCar;


}
