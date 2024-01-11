package com.example.skyNews.controller;

import com.example.skyNews.dto.request.*;
import com.example.skyNews.dto.response.*;
import com.example.skyNews.entity.Article;
import com.example.skyNews.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/create-article")
    public void CreateArticle(@RequestBody CreateArticleRequest request) throws ParseException {
        articleService.createArticle(request);
    }
    @GetMapping("/get-my-articles/{user-id}")
    public ResponseEntity<GetListArticlesResponse> getMyArticles(@PathVariable(value = "user-id") Long userId,
                                                                 @RequestParam String status,
                                                                 @RequestParam String searchQuery,
                                                                 @RequestParam int page,
                                                                 @RequestParam int pageSize) {
        GetMyArticles_ApprovedArticlesRequest request = new GetMyArticles_ApprovedArticlesRequest(status, searchQuery, page, pageSize);
        GetListArticlesResponse response = articleService.getMyArticles(userId, request);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/delete-not-send/{article-id}")
    public void deleteNotSendArticle(@PathVariable(value = "article-id") Long id) {
        articleService.deleteNotSendArticle(id);
    }
    @GetMapping("/detail/{article-id}")
    public ResponseEntity<GetDetailArticleResponse> getDetailArticle(@PathVariable(value = "article-id") Long articleId) {
        GetDetailArticleResponse response = articleService.getDetailArticle(articleId);
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/send-editor/{article-id}")
    public ResponseEntity<Article> sendArticleToEditor(@PathVariable(value = "article-id") Long id) {
        Article article = articleService.sendArticleToEditor(id);
        return ResponseEntity.ok().body(article);
    }
    @PutMapping("/edit-article/{article-id}")
    public void sendArticleToEditor(@PathVariable(value = "article-id") Long id,
                                    @RequestBody EditArticleRequest request) throws ParseException {
        articleService.editArticle(request, id);
    }
    @GetMapping("/pending-articles/{category}")
    public ResponseEntity<GetListArticlesResponse> getPendingArticlesByCategory(@PathVariable(value = "category") String categoryName,
                                                                                @RequestParam String searchQuery,
                                                                                @RequestParam int page,
                                                                                @RequestParam int pageSize) {
        GetPendingArticlesByCategoryRequest request = new GetPendingArticlesByCategoryRequest(searchQuery, page, pageSize);
        GetListArticlesResponse response = articleService.getPendingArticlesByCategory(categoryName, request);
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/approve/{article-id}/{action}")
    public ResponseEntity<Article> approveArticle(@PathVariable(value = "article-id") Long id,
                                                  @PathVariable(value = "action") String action) {
        Article article = articleService.approveArticle(id, action);
        return ResponseEntity.ok().body(article);
    }
    @GetMapping("/get-approved-articles/{category}")
    public ResponseEntity<GetListArticlesResponse> getApprovedArticles(@PathVariable(value = "category") String category,
                                                                       @RequestParam String status,
                                                                       @RequestParam String searchQuery,
                                                                       @RequestParam int page,
                                                                       @RequestParam int pageSize) {
        GetMyArticles_ApprovedArticlesRequest request = new GetMyArticles_ApprovedArticlesRequest(status, searchQuery, page, pageSize);
        GetListArticlesResponse response = articleService.getApprovedArticles(category, request);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/delete-posted/{article-id}")
    public void deletePostedArticle(@PathVariable(value = "article-id") Long id) {
        articleService.removePostedArticle(id);
    }
    @GetMapping("/articles-homepage")
    public ResponseEntity<GetListArticleHomePageResponse> getListArticleHomepage() {
        GetListArticleHomePageResponse response = articleService.getListArticleHomepage();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/articles-category-page")
    public ResponseEntity<GetListArticlesResponse> getListArticleCategoryTopicPage(@RequestParam String category,
                                                                                   @RequestParam String topic,
                                                                                   @RequestParam int page,
                                                                                   @RequestParam int pageSize) {
        System.out.println(topic);
        GetArticleCategoryTopicPageRequest request = new GetArticleCategoryTopicPageRequest(category, topic, page, pageSize);
        GetListArticlesResponse response = articleService.getListArticleCategoryTopicPage(request);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/articles-search-page")
    public ResponseEntity<GetListArticlesResponse> getArticleSearchPage(@RequestParam String category,
                                                                        @RequestParam String keyword,
                                                                        @RequestParam int page,
                                                                        @RequestParam int pageSize) {
        GetArticleBySearchRequest request = new GetArticleBySearchRequest(category, keyword, page, pageSize);
        GetListArticlesResponse response = articleService.getListArticleBySearch(request);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/newest")
    public ResponseEntity<GetListArticlesResponse> getArticleNewestPage(@RequestParam int page,
                                                                        @RequestParam int pageSize) {
        PaginationRequest request = new PaginationRequest(page, pageSize);
        GetListArticlesResponse response = articleService.getListArticleNewestPage(request);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/articles-most-view")
    public ResponseEntity<List<Article>> getArticleMostViewPage() {
        List<Article> response = articleService.getListArticleMostViewPage();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/articles-hot-news")
    public ResponseEntity<List<Article>> getArticleHotNewsPage(@RequestParam String date) throws ParseException {
        List<Article> response = articleService.getListArticleHotNewsPage(date);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/check-save/{user-id}/{article-id}")
    public ResponseEntity<CheckSaveNewsResponse> checkUserSaveArticle(@PathVariable(value = "user-id") Long userId,
                                                                      @PathVariable(value = "article-id") Long articleId) {
        CheckSaveNewsResponse response = articleService.checkUserSaveNews(userId, articleId);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/handle-save/{user-id}/{article-id}")
    public ResponseEntity<CheckSaveNewsResponse> handleUserSaveArticle(@PathVariable(value = "user-id") Long userId,
                                                                      @PathVariable(value = "article-id") Long articleId) {
        CheckSaveNewsResponse response = articleService.handleSaveArticle(userId, articleId);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/list-comment/{article-id}/{user-id}")
    public ResponseEntity<List<GetCommentArticleResponse>> getListCommentOfArticle(@PathVariable(value = "article-id") Long articleId,
                                                                                   @PathVariable(value = "user-id") Long userId) {
        List<GetCommentArticleResponse> response = articleService.getListCommentOfArticle(articleId, userId);
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/add-view/{article-id}/{user-id}")
    public void addViewArticle(@PathVariable(value = "article-id") Long articleId,
                               @PathVariable(value = "user-id") Long userId) {
        articleService.addViewArticle(articleId, userId);
    }
    @PostMapping("/add-comment/{article-id}/{user-id}/{comment-replied-id}")
    public void AddCommentArticle(@PathVariable(value = "article-id") Long articleId,
                                  @PathVariable(value = "user-id") Long userId,
                                  @PathVariable(value = "comment-replied-id") Long commentRepliedId,
                                  @RequestBody CommentArticleRequest request) {
        articleService.commentArticle(articleId, userId, commentRepliedId, request);
    }
    @GetMapping ("/handle-like-comment/{comment-id}/{user-id}")
    public ResponseEntity<String> handleLikeComment(@PathVariable(value = "comment-id") Long commentId,
                                                    @PathVariable(value = "user-id") Long userId) {
        articleService.handleLikeComment(commentId, userId);
        return ResponseEntity.ok().body("Thành công!");
    }
    @GetMapping("/saved-news/{user-id}")
    public ResponseEntity<GetListArticlesResponse> getSavedArticles(@PathVariable(value = "user-id") Long userId,
                                                          @RequestParam int page,
                                                          @RequestParam int pageSize) {
        PaginationRequest paginationRequest = new PaginationRequest(page, pageSize);
        GetListArticlesResponse response = articleService.getListSavedArticles(userId, paginationRequest);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/un-save/{article-id}/{user-id}")
    public void unSaveArticle(@PathVariable(value = "article-id") Long articleId,
                              @PathVariable(value = "user-id") Long userId) {
        articleService.unSaveArticle(articleId, userId);
    }
    @GetMapping("/viewed-news/{user-id}")
    public ResponseEntity<GetListArticleViewedResponse> getViewedArticles(@PathVariable(value = "user-id") Long userId,
                                                                          @RequestParam int page,
                                                                          @RequestParam int pageSize) {
        PaginationRequest paginationRequest = new PaginationRequest(page, pageSize);
        GetListArticleViewedResponse response = articleService.getListViewedArticles(userId, paginationRequest);
        return ResponseEntity.ok().body(response);
    }
}
