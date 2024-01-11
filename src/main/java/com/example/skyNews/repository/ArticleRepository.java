package com.example.skyNews.repository;

import com.example.skyNews.entity.Article;
import com.example.skyNews.entity.Category;
import com.example.skyNews.entity.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCategory(Category category);
    List<Article> findByTopic(Topic topic);
    @Query("SELECT u FROM Article u WHERE (:status = '' OR u.approvalStatus = :status) " +
            "AND u.title LIKE %:searchQuery% " +
            "AND u.authorId = :authorId ORDER BY u.createTimestamp desc")
    List<Article> getMyArticles(@Param("authorId") Long authorId,
                                @Param("status") String status,
                                @Param("searchQuery") String searchQuery,
                                Pageable pageable);
    @Query("SELECT COUNT(u) FROM Article u WHERE (:status = '' OR u.approvalStatus = :status) " +
            "AND u.title LIKE %:searchQuery% " +
            "AND u.authorId = :authorId")
    int countMyArticles(@Param("authorId") Long authorId,
                        @Param("status") String status,
                        @Param("searchQuery") String searchQuery);
    @Query("SELECT u FROM Article u WHERE u.title LIKE %:searchQuery% " +
            "AND u.category.name = :category AND u.approvalStatus = 'pending' ORDER BY u.sendEditorTimestamp desc")
    List<Article> getPendingArticlesByCategory(@Param("category") String category,
                                               @Param("searchQuery") String searchQuery,
                                               Pageable pageable);

    @Query("SELECT COUNT(u) FROM Article u WHERE u.title LIKE %:searchQuery% " +
            "AND u.category.name = :category AND u.approvalStatus = 'pending'")
    int countPendingArticlesByCategory(@Param("category") String category,
                                       @Param("searchQuery") String searchQuery);
    @Query("SELECT u FROM Article u WHERE ((:status = '' AND (u.approvalStatus = 'accept' OR u.approvalStatus = 'decline')) OR u.approvalStatus = :status) " +
            "AND u.title LIKE %:searchQuery% AND u.category.name = :category ORDER BY u.decisionTimestamp desc")
    List<Article> getApprovedArticles(@Param("category") String category,
                                      @Param("status") String status,
                                      @Param("searchQuery") String searchQuery,
                                      Pageable pageable);
    @Query("SELECT COUNT(u) FROM Article u WHERE ((:status = '' AND (u.approvalStatus = 'accept' OR u.approvalStatus = 'decline')) OR u.approvalStatus = :status) " +
            "AND u.title LIKE %:searchQuery% AND u.category.name = :category")
    int countApprovedArticles(@Param("category") String category,
                              @Param("status") String status,
                              @Param("searchQuery") String searchQuery);

    @Query("SELECT u FROM Article u WHERE u.isHot = 1 AND u.approvalStatus = 'accept' ORDER BY u.decisionTimestamp desc")
    List<Article> getListArticleMixUserSite(Pageable pageable);
    @Query("SELECT u FROM Article u WHERE u.category.name = :category AND u.approvalStatus = 'accept' ORDER BY u.decisionTimestamp desc")
    List<Article> getListArticleCategoryUserSite(@Param("category") String category,
                                                 Pageable pageable);
    @Query("SELECT u FROM Article u WHERE u.category.name = :category AND u.approvalStatus = 'accept' ORDER BY u.decisionTimestamp DESC")
    List<Article> getListArticleCategoryPage(@Param("category") String category,
                                             Pageable pageable);
    @Query("SELECT COUNT(u) FROM Article u WHERE u.category.name = :category AND u.approvalStatus = 'accept'")
    int countListArticleCategoryPage(@Param("category") String category);
    @Query("SELECT u FROM Article u WHERE u.category.name = :category AND u.topic.name = :topic AND u.approvalStatus = 'accept' ORDER BY u.decisionTimestamp DESC")
    List<Article> getListArticleTopicPage(@Param("category") String category,
                                          @Param("topic") String topic,
                                          Pageable pageable);
    @Query("SELECT COUNT(u) FROM Article u WHERE u.category.name = :category AND u.topic.name = :topic AND u.approvalStatus = 'accept'")
    int countListArticleTopicPage(@Param("category") String category,
                                  @Param("topic") String topic);
    @Query("SELECT u FROM Article u WHERE (:category = '' OR u.category.name = :category) AND u.title LIKE %:keyword% AND u.approvalStatus = 'accept' ORDER BY u.decisionTimestamp DESC")
    List<Article> getListArticleSearchPage(@Param("category") String category,
                                           @Param("keyword") String keyword,
                                           Pageable pageable);
    @Query("SELECT COUNT(u) FROM Article u WHERE (:category = '' OR u.category.name = :category) AND u.title LIKE %:keyword% AND u.approvalStatus = 'accept'")
    int countListArticleSearchPage(@Param("category") String category,
                                   @Param("keyword") String keyword);
    @Query("SELECT u FROM Article u WHERE u.approvalStatus = 'accept' ORDER BY u.decisionTimestamp DESC")
    List<Article> getListArticleNewestPage(Pageable pageable);
    @Query("SELECT COUNT(u) FROM Article u WHERE u.approvalStatus = 'accept'")
    int countListArticleNewestPage();
    @Query("SELECT u FROM Article u WHERE u.approvalStatus = 'accept' AND u.decisionTimestamp BETWEEN :startDate AND :endDate ORDER BY u.view desc, u.decisionTimestamp DESC")
    List<Article> getListArticleMostViewPage(@Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate,
                                             Pageable pageable);
    @Query("SELECT u FROM Article u WHERE u.approvalStatus = 'accept' AND u.isHot = 1 AND DATE(u.decisionTimestamp) = :selectedDate ORDER BY u.decisionTimestamp DESC")
    List<Article> getListArticleHotNewsPage(@Param("selectedDate") Date selectedDate);
}
