package com.example.skyNews.repository;

import com.example.skyNews.entity.Comment;
import com.example.skyNews.entity.Like;
import com.example.skyNews.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByComment(Comment comment);
    Like findByCommentAndLiker(Comment comment, User liker);
}
