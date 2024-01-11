package com.example.skyNews.controller;

import com.example.skyNews.dto.request.AddRenameCategoryTopicRequest;
import com.example.skyNews.dto.response.ErrorResponse;
import com.example.skyNews.dto.response.GetListCategoryTopicResponse;
import com.example.skyNews.entity.Category;
import com.example.skyNews.entity.Topic;
import com.example.skyNews.service.CategoryTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category-topic")
@RequiredArgsConstructor
public class CategoryTopicController {
    private final CategoryTopicService categoryTopicService;

    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestBody AddRenameCategoryTopicRequest request) {
        try {
            categoryTopicService.addCategory(request);
            return ResponseEntity.ok().body("Thêm danh mục thành công!");
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping("/add-topic/{categoryId}")
    public ResponseEntity<?> addTopic(@RequestBody AddRenameCategoryTopicRequest request,
                                      @PathVariable(value = "categoryId") Long categoryId) {
        try {
            categoryTopicService.addTopicToCategory(request, categoryId);
            return ResponseEntity.ok().body("Thêm chủ đề thành công!");
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping
    public ResponseEntity<GetListCategoryTopicResponse> getListCategoryTopic() {
        GetListCategoryTopicResponse listCategoryTopic = categoryTopicService.getListCategoryTopic();
        return ResponseEntity.ok().body(listCategoryTopic);
    }
    @PutMapping("/rename-category/{category-id}")
    public ResponseEntity<?> renameCategory(@RequestBody AddRenameCategoryTopicRequest request,
                                            @PathVariable(value = "category-id") Long categoryId) {
        try {
            categoryTopicService.renameCategory(categoryId, request);
            return ResponseEntity.ok().body("Đổi tên danh mục thành công!");
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PutMapping("/rename-topic/{topic-id}")
    public ResponseEntity<?> renameTopic(@RequestBody AddRenameCategoryTopicRequest request,
                                         @PathVariable(value = "topic-id") Long topicId) {
        try {
            categoryTopicService.renameTopic(topicId, request);
            return ResponseEntity.ok().body("Đổi tên chủ đề thành công!");
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @DeleteMapping("/delete-category/{category-id}")
    public void deleteCategory(@PathVariable(value = "category-id") Long categoryId) {
        categoryTopicService.deleteCategory(categoryId);
    }
    @DeleteMapping("/delete-topic/{topic-id}")
    public void deleteTopic(@PathVariable(value = "topic-id") Long topicId) {
        categoryTopicService.deleteTopic(topicId);
    }
    @GetMapping("/get-list-category")
    public ResponseEntity<List<Category>> getListCategory() {
        List<Category> categories = categoryTopicService.getListCategory();
        return ResponseEntity.ok().body(categories);
    }
    @GetMapping("/get-list-topic/{category-name}")
    public ResponseEntity<List<Topic>> getListTopicByCategory(@PathVariable(value = "category-name") String name) {
        List<Topic> listTopic = categoryTopicService.getListTopicByCategory(name);
        return ResponseEntity.ok().body(listTopic);
    }
}
