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
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String mainImage;
    private String content;
    private String location;
    private Date dateNews;
    private Long authorId;
    private String authorName;

    private Integer isHot;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private Date createTimestamp;
    private String approvalStatus;
    private Long view;
    private Date sendEditorTimestamp;
    private Date decisionTimestamp;
}
