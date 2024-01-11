package com.example.skyNews.service;

import com.example.skyNews.dto.request.*;
import com.example.skyNews.dto.response.*;
import com.example.skyNews.entity.Article;

import java.text.ParseException;
import java.util.List;

public interface ArticleService {
    void createArticle(CreateArticleRequest request) throws ParseException;
    GetListArticlesResponse getMyArticles(Long userId, GetMyArticles_ApprovedArticlesRequest request);
    void deleteNotSendArticle(Long id);
    GetDetailArticleResponse getDetailArticle(Long id);
    Article sendArticleToEditor(Long id);
    void editArticle(EditArticleRequest request, Long id) throws ParseException;
    GetListArticlesResponse getPendingArticlesByCategory(String category, GetPendingArticlesByCategoryRequest request);
    Article approveArticle(Long id, String approveAction);
    GetListArticlesResponse getApprovedArticles(String category, GetMyArticles_ApprovedArticlesRequest request);
    void removePostedArticle(Long id);
    GetListArticleHomePageResponse getListArticleHomepage();
    GetListArticlesResponse getListArticleCategoryTopicPage(GetArticleCategoryTopicPageRequest request);
    GetListArticlesResponse getListArticleBySearch(GetArticleBySearchRequest request);
    GetListArticlesResponse getListArticleNewestPage(PaginationRequest request);
    List<Article> getListArticleMostViewPage();
    List<Article> getListArticleHotNewsPage(String date) throws ParseException;
    CheckSaveNewsResponse checkUserSaveNews(Long userId, Long articleId);
    CheckSaveNewsResponse handleSaveArticle(Long userId, Long articleId);
    List<GetCommentArticleResponse> getListCommentOfArticle(Long articleId, Long userId);
    void addViewArticle(Long articleId, Long userId);
    void commentArticle(Long articleId, Long userId, Long idCommentReplied, CommentArticleRequest request);
    void handleLikeComment(Long commentId, Long userId);
    GetListArticlesResponse getListSavedArticles(Long userId, PaginationRequest request);
    void unSaveArticle(Long articleId, Long userId);
    GetListArticleViewedResponse getListViewedArticles(Long userId, PaginationRequest request);
}
