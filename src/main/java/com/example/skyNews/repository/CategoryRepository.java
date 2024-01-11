package com.example.skyNews.repository;

import com.example.skyNews.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    Category findByName(String name);
}
