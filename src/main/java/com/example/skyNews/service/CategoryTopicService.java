package com.example.skyNews.service;

import com.example.skyNews.dto.request.AddRenameCategoryTopicRequest;
import com.example.skyNews.dto.response.GetListCategoryTopicResponse;
import com.example.skyNews.entity.Article;
import com.example.skyNews.entity.Category;
import com.example.skyNews.entity.Topic;

import java.util.List;

public interface CategoryTopicService {
    void addCategory(AddRenameCategoryTopicRequest request);
    void addTopicToCategory(AddRenameCategoryTopicRequest request, Long categoryId);
    GetListCategoryTopicResponse getListCategoryTopic();
    List<Category> getListCategory();
    void renameCategory(Long categoryId, AddRenameCategoryTopicRequest request);
    void renameTopic(Long topicId, AddRenameCategoryTopicRequest request);
    void deleteCategory(Long id);
    void deleteTopic(Long id);
    void deleteArticlesInCategoryTopic(List<Article> articlesToDelete);
    List<Topic> getListTopicByCategory(String name);
}
