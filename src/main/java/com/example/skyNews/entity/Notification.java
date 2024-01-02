package com.example.skyNews.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String type;

    @ManyToOne
    @JoinColumn(name = "like_id")
    private Like like;

    @ManyToOne
    @JoinColumn(name = "reply_comment_id")
    private Comment replyComment;

    private Date timestamp;
    private Boolean isNew;
}
