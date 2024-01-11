package com.example.skyNews.repository;

import com.example.skyNews.entity.Category;
import com.example.skyNews.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    boolean existsByCategoryAndName(Category category, String name);
    List<Topic> findByCategory(Category category);
    Topic findByNameAndCategory(String name, Category category);
}
