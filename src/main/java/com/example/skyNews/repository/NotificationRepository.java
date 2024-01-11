package com.example.skyNews.repository;

import com.example.skyNews.entity.Comment;
import com.example.skyNews.entity.Like;
import com.example.skyNews.entity.Notification;
import com.example.skyNews.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByLikeComment(Like like);
    List<Notification> findByReplyComment(Comment comment);
    List<Notification> findByUserOrderByTimestampDesc(User user);
}
