package com.example.skyNews.repository;

import com.example.skyNews.entity.Article;
import com.example.skyNews.entity.ArticleView;
import com.example.skyNews.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByArticle(Article article);
}
