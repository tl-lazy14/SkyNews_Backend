package com.example.skyNews.service;

import com.example.skyNews.entity.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getListNotification(Long userId);
    void handleReadNotification(Long id);
}
