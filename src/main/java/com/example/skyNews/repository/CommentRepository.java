package com.example.skyNews.repository;

import com.example.skyNews.entity.Article;
import com.example.skyNews.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticle(Article article);
    List<Comment> findByParentComment(Comment comment);
    List<Comment> findAllByArticleAndParentCommentIsNull(Article article);
}
