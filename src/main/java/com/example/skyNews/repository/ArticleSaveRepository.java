package com.example.skyNews.repository;

import com.example.skyNews.entity.Article;
import com.example.skyNews.entity.ArticleSave;
import com.example.skyNews.entity.ArticleView;
import com.example.skyNews.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleSaveRepository extends JpaRepository<ArticleSave, Long> {
    List<ArticleSave> findByArticle(Article article);
    ArticleSave findByUserAndArticle(User user, Article article);
    boolean existsByUserAndArticle(User user, Article article);
    List<ArticleSave> findByUserOrderBySaveTimestampDesc(User user, Pageable pageable);
    int countByUser(User user);
}
