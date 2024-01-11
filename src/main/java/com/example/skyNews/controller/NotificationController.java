package com.example.skyNews.controller;

import com.example.skyNews.dto.request.GetMyArticles_ApprovedArticlesRequest;
import com.example.skyNews.dto.response.GetListArticlesResponse;
import com.example.skyNews.entity.Notification;
import com.example.skyNews.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/get-list-notification/{user-id}")
    public ResponseEntity<List<Notification>> getListNotification(@PathVariable(value = "user-id") Long userId) {
        List<Notification> notificationList = notificationService.getListNotification(userId);
        return ResponseEntity.ok().body(notificationList);
    }
    @PutMapping("/read-notification/{notification-id}")
    public void handleReadNotification(@PathVariable(value = "notification-id") Long id) {
        notificationService.handleReadNotification(id);
    }
}
