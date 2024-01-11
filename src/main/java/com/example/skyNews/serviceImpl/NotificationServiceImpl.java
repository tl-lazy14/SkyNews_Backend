package com.example.skyNews.serviceImpl;

import com.example.skyNews.entity.Notification;
import com.example.skyNews.entity.User;
import com.example.skyNews.repository.*;
import com.example.skyNews.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getListNotification(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return notificationRepository.findByUserOrderByTimestampDesc(user);
    }

    @Override
    public void handleReadNotification(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notification.setIsNew(0);
        notificationRepository.save(notification);
    }
}
