package com.example.skyNews.repository;

import com.example.skyNews.entity.Article;
import com.example.skyNews.entity.ArticleView;
import com.example.skyNews.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleViewRepository extends JpaRepository<ArticleView, Long> {
    List<ArticleView> findByArticle(Article article);
    boolean existsByArticleAndViewer(Article article, User viewer);
    List<ArticleView> findByViewerOrderByViewTimestampDesc(User viewer, Pageable pageable);
    int countByViewer(User viewer);
}
