package com.example.skyNews.serviceImpl;

import com.example.skyNews.dto.request.AddRenameCategoryTopicRequest;
import com.example.skyNews.dto.response.CategoryResponse;
import com.example.skyNews.dto.response.GetListCategoryTopicResponse;
import com.example.skyNews.entity.*;
import com.example.skyNews.repository.*;
import com.example.skyNews.service.CategoryTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryTopicServiceImpl implements CategoryTopicService {
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final ArticleViewRepository articleViewRepository;
    private final ArticleSaveRepository articleSaveRepository;
    private final TagRepository tagRepository;

    @Override
    public void addCategory(AddRenameCategoryTopicRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại");
        }
        Category newCategory = new Category();
        newCategory.setName(request.getName());
        categoryRepository.save(newCategory);
    }

    @Override
    public void addTopicToCategory(AddRenameCategoryTopicRequest request, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        if (topicRepository.existsByCategoryAndName(category, request.getName())) {
            throw new IllegalArgumentException("Tên chủ đề đã tồn tại trong danh mục");
        }
        Topic newTopic = new Topic();
        newTopic.setName(request.getName());
        newTopic.setCategory(category);
        topicRepository.save(newTopic);
    }

    @Override
    public GetListCategoryTopicResponse getListCategoryTopic() {
        List<Category> listCategory = categoryRepository.findAll();
        List<CategoryResponse> responseList = new ArrayList<>();
        for (Category category : listCategory) {
            List<Topic> listTopic = topicRepository.findByCategory(category);
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .listTopic(listTopic)
                    .build();
            responseList.add(categoryResponse);
        }
        return GetListCategoryTopicResponse.builder()
                .listCategory(responseList)
                .numCategory(listCategory.size())
                .build();
    }
    @Transactional
    @Override
    public void renameCategory(Long categoryId, AddRenameCategoryTopicRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại");
        }
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        Optional<User> editorOptional = userRepository.findByEditorCategory(category.getName());
        editorOptional.ifPresent(editor -> {
            editor.setEditorCategory(request.getName());
            userRepository.save(editor);
        });
        category.setName(request.getName());
        categoryRepository.save(category);
    }

    @Override
    public void renameTopic(Long topicId, AddRenameCategoryTopicRequest request) {
        Topic topic = topicRepository.findById(topicId).orElseThrow();
        if (topicRepository.existsByCategoryAndName(topic.getCategory(), request.getName())) {
            throw new IllegalArgumentException("Tên chủ đề đã tồn tại trong danh mục");
        }
        topic.setName(request.getName());
        topicRepository.save(topic);
    }
    @Override
    public void deleteArticlesInCategoryTopic(List<Article> articlesToDelete) {
        if (!articlesToDelete.isEmpty()) {
            articlesToDelete.forEach(article -> {
                List<Comment> allCommentOfArticle = commentRepository.findByArticle(article);
                if (!allCommentOfArticle.isEmpty()) {
                    allCommentOfArticle.forEach(comment -> {
                        List<Like> likesToDelete = likeRepository.findByComment(comment);
                        if (!likesToDelete.isEmpty()) {
                            likesToDelete.forEach(like -> {
                                List<Notification> notificationsLike = notificationRepository.findByLikeComment(like);
                                notificationRepository.deleteAll(notificationsLike);
                            });
                            likeRepository.deleteAll(likesToDelete);
                        }
                    });
                    List<Comment> allParentComment = commentRepository.findAllByArticleAndParentCommentIsNull(article);
                    allParentComment.forEach(parentComment -> {
                        List<Comment> childComments = commentRepository.findByParentComment(parentComment);
                        if (!childComments.isEmpty()) {
                            childComments.forEach(childComment -> {
                                List<Notification> notificationsLike = notificationRepository.findByReplyComment(childComment);
                                notificationRepository.deleteAll(notificationsLike);
                            });
                            commentRepository.deleteAll(childComments);
                        }
                        commentRepository.deleteById(parentComment.getId());
                    });
                }
                List<ArticleView> articleViewList = articleViewRepository.findByArticle(article);
                if (!articleViewList.isEmpty()) articleViewRepository.deleteAll(articleViewList);

                List<ArticleSave> articleSaveList = articleSaveRepository.findByArticle(article);
                if (!articleSaveList.isEmpty()) articleSaveRepository.deleteAll(articleSaveList);

                List<Tag> tagList = tagRepository.findByArticle(article);
                if (!tagList.isEmpty()) tagRepository.deleteAll(tagList);
            });
            articleRepository.deleteAll(articlesToDelete);
        }
    }
    @Transactional
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow();

        List<Article> articlesToDelete = articleRepository.findByCategory(category);
        deleteArticlesInCategoryTopic(articlesToDelete);

        List<Topic> topicsToDelete = topicRepository.findByCategory(category);
        topicRepository.deleteAll(topicsToDelete);

        categoryRepository.deleteById(id);
    }
    @Transactional
    @Override
    public void deleteTopic(Long id) {
        Topic topic = topicRepository.findById(id).orElseThrow();
        List<Article> articlesToDelete = articleRepository.findByTopic(topic);
        deleteArticlesInCategoryTopic(articlesToDelete);
        topicRepository.deleteById(id);
    }

    @Override
    public List<Category> getListCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Topic> getListTopicByCategory(String name) {
        Category category = categoryRepository.findByName(name);
        return topicRepository.findByCategory(category);
    }
}
