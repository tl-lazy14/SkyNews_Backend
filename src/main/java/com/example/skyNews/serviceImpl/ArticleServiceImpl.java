package com.example.skyNews.serviceImpl;

import com.example.skyNews.dto.request.*;
import com.example.skyNews.dto.response.*;
import com.example.skyNews.entity.*;
import com.example.skyNews.repository.*;
import com.example.skyNews.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final ArticleSaveRepository articleSaveRepository;
    private final ArticleViewRepository articleViewRepository;

    @Override
    public void createArticle(CreateArticleRequest request) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Category category = categoryRepository.findByName(request.getCategory());
        User author = userRepository.findById(request.getAuthorId()).orElseThrow();

        Article newArticle = new Article();
        newArticle.setTitle(request.getTitle());
        newArticle.setMainImage(request.getMainImage());
        newArticle.setContent(request.getContent());
        newArticle.setLocation(request.getLocation());
        newArticle.setDateNews(dateFormat.parse(request.getDate()));
        newArticle.setAuthorId(author.getId());
        newArticle.setAuthorName(author.getName());
        newArticle.setIsHot(request.getIsHot());
        newArticle.setCategory(category);
        if (request.getTopic() != null) {
            Topic topic = topicRepository.findByNameAndCategory(request.getTopic(), category);
            newArticle.setTopic(topic);
        }
        newArticle.setCreateTimestamp(new Date());
        newArticle.setApprovalStatus("not-send");
        newArticle.setView(0L);
        articleRepository.save(newArticle);

        if (!request.getTags().isEmpty()) {
            createTagsForArticle(newArticle, request.getTags());
        }
    }

    @Override
    public GetListArticlesResponse getMyArticles(Long userId, GetMyArticles_ApprovedArticlesRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
        List<Article> listMyArticle = articleRepository.getMyArticles(
                userId,
                request.getStatus(),
                request.getSearchQuery(),
                pageable
        );
        int numArticle = articleRepository.countMyArticles(userId, request.getStatus(), request.getSearchQuery());
        return GetListArticlesResponse.builder()
                .listArticle(listMyArticle)
                .numArticle(numArticle)
                .build();
    }
    @Transactional
    @Override
    public void deleteNotSendArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        List<Tag> listTagOfArticle = tagRepository.findByArticle(article);
        tagRepository.deleteAll(listTagOfArticle);
        articleRepository.deleteById(id);
    }

    @Override
    public GetDetailArticleResponse getDetailArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        List<Tag> listTag = tagRepository.findByArticle(article);
        List<String> listTagName = new ArrayList<>();
        listTag.forEach(tag -> listTagName.add(tag.getTagName()));
        return GetDetailArticleResponse.builder()
                .article(article)
                .tags(listTagName)
                .build();
    }
    @Transactional
    @Override
    public Article sendArticleToEditor(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        article.setApprovalStatus("pending");
        article.setSendEditorTimestamp(new Date());
        articleRepository.save(article);
        return  article;
    }
    @Transactional
    @Override
    public void editArticle(EditArticleRequest request, Long id) throws ParseException {
        Article article = articleRepository.findById(id).orElseThrow();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Category category = categoryRepository.findByName(request.getCategory());

        article.setTitle(request.getTitle());
        article.setMainImage(request.getMainImage());
        article.setContent(request.getContent());
        article.setLocation(request.getLocation());
        article.setDateNews(dateFormat.parse(request.getDate()));
        article.setIsHot(request.getIsHot());
        article.setCategory(category);
        if (request.getTopic() != null) {
            Topic topic = topicRepository.findByNameAndCategory(request.getTopic(), category);
            article.setTopic(topic);
        }
        articleRepository.save(article);

        if (!request.getTags().isEmpty()) {
            List<Tag> listCurrentTag = tagRepository.findByArticle(article);
            tagRepository.deleteAll(listCurrentTag);
            createTagsForArticle(article, request.getTags());
        }
    }

    private void createTagsForArticle(Article article, String tags) {
        List<String> listNewTag = new ArrayList<>();
        if (tags.contains("/")) {
            String[] arrayResult = tags.split("/");
            for (String str : arrayResult) {
                listNewTag.add(str.trim());
            }
        } else {
            listNewTag.add(tags.trim());
        }
        for (String str : listNewTag) {
            Tag newTag = new Tag();
            newTag.setTagName(str);
            newTag.setArticle(article);
            tagRepository.save(newTag);
        }
    }

    @Override
    public GetListArticlesResponse getPendingArticlesByCategory(String category, GetPendingArticlesByCategoryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
        List<Article> listPendingArticle = articleRepository.getPendingArticlesByCategory(
                category,
                request.getSearchQuery(),
                pageable
        );
        int numArticle = articleRepository.countPendingArticlesByCategory(category, request.getSearchQuery());
        return GetListArticlesResponse.builder()
                .listArticle(listPendingArticle)
                .numArticle(numArticle)
                .build();
    }

    @Override
    public Article approveArticle(Long id, String approveAction) {
        Article article = articleRepository.findById(id).orElseThrow();
        article.setApprovalStatus(approveAction);
        article.setDecisionTimestamp(new Date());
        articleRepository.save(article);
        return  article;
    }

    @Override
    public GetListArticlesResponse getApprovedArticles(String category, GetMyArticles_ApprovedArticlesRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
        List<Article> listApprovedArticle = articleRepository.getApprovedArticles(
                category,
                request.getStatus(),
                request.getSearchQuery(),
                pageable
        );
        int numArticle = articleRepository.countApprovedArticles(category, request.getStatus(), request.getSearchQuery());
        return GetListArticlesResponse.builder()
                .listArticle(listApprovedArticle)
                .numArticle(numArticle)
                .build();
    }

    @Override
    public void removePostedArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
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

        articleRepository.deleteById(id);
    }

    @Override
    public GetListArticleHomePageResponse getListArticleHomepage() {
        Pageable pageable20 = PageRequest.of(0, 20);
        Pageable pageable5 = PageRequest.of(0, 5);
        Pageable pageable7 = PageRequest.of(0, 7);
        Pageable pageable2 = PageRequest.of(0, 2);
        List<Article> listArticleMix = articleRepository.getListArticleMixUserSite(pageable20);
        Collections.shuffle(listArticleMix);

        List<Article> articleBusiness = articleRepository.getListArticleCategoryUserSite("Kinh doanh", pageable5);
        Collections.shuffle(articleBusiness);
        List<Article> articleRealEstate = articleRepository.getListArticleCategoryUserSite("Bất động sản", pageable5);
        Collections.shuffle(articleRealEstate);
        List<Article> articleSport = articleRepository.getListArticleCategoryUserSite("Thể thao", pageable5);
        Collections.shuffle(articleSport);
        List<Article> articleEntertainment = articleRepository.getListArticleCategoryUserSite("Giải trí", pageable5);
        Collections.shuffle(articleEntertainment);
        List<Article> articleHealth = articleRepository.getListArticleCategoryUserSite("Sức khỏe", pageable5);
        Collections.shuffle(articleHealth);
        List<Article> articleLife = articleRepository.getListArticleCategoryUserSite("Đời sống", pageable5);
        Collections.shuffle(articleLife);
        List<Article> articleEducation = articleRepository.getListArticleCategoryUserSite("Giáo dục", pageable5);
        Collections.shuffle(articleEducation);

        List<Article> articleScience = articleRepository.getListArticleCategoryUserSite("Khoa học", pageable7);
        Collections.shuffle(articleScience);
        List<Article> articleDigitizing = articleRepository.getListArticleCategoryUserSite("Số hóa", pageable7);
        Collections.shuffle(articleDigitizing);
        List<Article> articleTourism = articleRepository.getListArticleCategoryUserSite("Du lịch", pageable7);
        Collections.shuffle(articleTourism);
        List<Article> articleCar = articleRepository.getListArticleCategoryUserSite("Xe", pageable7);
        Collections.shuffle(articleCar);

        List<Article> articleNews = articleRepository.getListArticleCategoryUserSite("Thời sự", pageable2);
        Collections.shuffle(articleNews);
        List<Article> articleWorld = articleRepository.getListArticleCategoryUserSite("Thế giới", pageable2);
        Collections.shuffle(articleWorld);
        List<Article> articleLaw = articleRepository.getListArticleCategoryUserSite("Pháp luật", pageable2);
        Collections.shuffle(articleLaw);

        return  GetListArticleHomePageResponse.builder()
                .listArticleMix(listArticleMix)
                .articleBusiness(articleBusiness)
                .articleRealEstate(articleRealEstate)
                .articleTourism(articleTourism)
                .articleDigitizing(articleDigitizing)
                .articleCar(articleCar)
                .articleEducation(articleEducation)
                .articleScience(articleScience)
                .articleEntertainment(articleEntertainment)
                .articleHealth(articleHealth)
                .articleLaw(articleLaw)
                .articleLife(articleLife)
                .articleNews(articleNews)
                .articleSport(articleSport)
                .articleWorld(articleWorld)
                .build();
    }

    @Override
    public GetListArticlesResponse getListArticleCategoryTopicPage(GetArticleCategoryTopicPageRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
        List<Article> listArticle;
        int numArticle;
        if (Objects.equals(request.getTopic(), "no-topic")) {
            listArticle = articleRepository.getListArticleCategoryPage(
                    request.getCategory(),
                    pageable
            );
            numArticle = articleRepository.countListArticleCategoryPage(request.getCategory());
        } else {
            listArticle = articleRepository.getListArticleTopicPage(
                    request.getCategory(),
                    request.getTopic(),
                    pageable
            );
            numArticle = articleRepository.countListArticleTopicPage(request.getCategory(), request.getTopic());
        }
        return GetListArticlesResponse.builder()
                .listArticle(listArticle)
                .numArticle(numArticle)
                .build();
    }

    @Override
    public GetListArticlesResponse getListArticleBySearch(GetArticleBySearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
        List<Article> listArticle = articleRepository.getListArticleSearchPage(
                request.getCategory(),
                request.getKeyword(),
                pageable
        );
        int numArticle = articleRepository.countListArticleSearchPage(request.getCategory(), request.getKeyword());
        return GetListArticlesResponse.builder()
                .listArticle(listArticle)
                .numArticle(numArticle)
                .build();
    }

    @Override
    public GetListArticlesResponse getListArticleNewestPage(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
        List<Article> listArticle = articleRepository.getListArticleNewestPage(pageable);
        int numArticle = articleRepository.countListArticleNewestPage();
        return GetListArticlesResponse.builder()
                .listArticle(listArticle)
                .numArticle(numArticle)
                .build();
    }

    @Override
    public List<Article> getListArticleMostViewPage() {
        Pageable pageable = PageRequest.of(0, 30);
        Instant instantEndDate = new Date().toInstant();
        Instant instantStartDate = instantEndDate.minus(Duration.ofDays(5));
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(instantStartDate, ZoneId.systemDefault());
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(instantEndDate, ZoneId.systemDefault());
        Date startDate = Date.from(startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return articleRepository.getListArticleMostViewPage(startDate, endDate, pageable);
    }

    @Override
    public List<Article> getListArticleHotNewsPage(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse(date);
        return articleRepository.getListArticleHotNewsPage(parsedDate);
    }

    @Override
    public CheckSaveNewsResponse checkUserSaveNews(Long userId, Long articleId) {
        CheckSaveNewsResponse response = new CheckSaveNewsResponse();

        User user = userRepository.findById(userId).orElseThrow();
        Article article = articleRepository.findById(articleId).orElseThrow();
        ArticleSave articleSave = articleSaveRepository.findByUserAndArticle(user, article);
        response.setIsSave(articleSave != null ? 1 : 0);
        return response;
    }

    @Override
    public CheckSaveNewsResponse handleSaveArticle(Long userId, Long articleId) {
        CheckSaveNewsResponse response = new CheckSaveNewsResponse();

        User user = userRepository.findById(userId).orElseThrow();
        Article article = articleRepository.findById(articleId).orElseThrow();
        ArticleSave articleSave = articleSaveRepository.findByUserAndArticle(user, article);
        if (articleSave != null) {
            articleSaveRepository.deleteById(articleSave.getId());
            response.setIsSave(0);
        } else {
            ArticleSave newArticleSave = new ArticleSave();
            newArticleSave.setArticle(article);
            newArticleSave.setUser(user);
            newArticleSave.setSaveTimestamp(new Date());
            articleSaveRepository.save(newArticleSave);
            response.setIsSave(1);
        }
        return response;
    }

    @Override
    public List<GetCommentArticleResponse> getListCommentOfArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        List<GetCommentArticleResponse> responseList = new ArrayList<>();
        List<Comment> allParentCommentOfArticle = commentRepository.findAllByArticleAndParentCommentIsNull(article);
        allParentCommentOfArticle.forEach(parentComment -> {
            GetCommentArticleResponse response = new GetCommentArticleResponse();

            // Thông tin về comment cha
            CommentInfoResponse infoParentComment = new CommentInfoResponse();
            infoParentComment.setId(parentComment.getId());
            infoParentComment.setCommenter(parentComment.getCommenter());
            infoParentComment.setCommentText(parentComment.getCommentText());
            infoParentComment.setCommentTimestamp(parentComment.getCommentTimestamp());
            infoParentComment.setUsernameTag("");

            List<Like> listLikeParentComment = likeRepository.findByComment(parentComment);
            infoParentComment.setNumLike(listLikeParentComment.size());

            if (userId != 0) {
                User user = userRepository.findById(userId).orElseThrow();
                Like checkUserLikeComment = likeRepository.findByCommentAndLiker(parentComment, user);
                if (checkUserLikeComment != null) infoParentComment.setIsUserLike(1);
                else infoParentComment.setIsUserLike(0);
            } else infoParentComment.setIsUserLike(0);

            // Danh sách comment con
            List<Comment> listCommentChild = commentRepository.findByParentComment(parentComment);
            List<CommentInfoResponse> listInfoChildComment = new ArrayList<>();
            listCommentChild.forEach(commentChild -> {
                CommentInfoResponse infoChildComment = new CommentInfoResponse();
                infoChildComment.setId(commentChild.getId());
                infoChildComment.setCommenter(commentChild.getCommenter());
                infoChildComment.setCommentText(commentChild.getCommentText());
                infoChildComment.setCommentTimestamp(commentChild.getCommentTimestamp());
                infoChildComment.setUsernameTag(commentChild.getUsernameTag());

                List<Like> listLikeChildComment = likeRepository.findByComment(commentChild);
                infoChildComment.setNumLike(listLikeChildComment.size());

                if (userId != 0) {
                    User user = userRepository.findById(userId).orElseThrow();
                    Like checkUserLikeComment = likeRepository.findByCommentAndLiker(commentChild, user);
                    if (checkUserLikeComment != null) infoChildComment.setIsUserLike(1);
                    else infoChildComment.setIsUserLike(0);
                } else infoChildComment.setIsUserLike(0);

                listInfoChildComment.add(infoChildComment);
            });
            response.setParentComment(infoParentComment);
            response.setChildComments(listInfoChildComment);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public void addViewArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.setView(article.getView() + 1);
        articleRepository.save(article);
        if (userId != 0) {
            User user = userRepository.findById(userId).orElseThrow();
            if (!articleViewRepository.existsByArticleAndViewer(article, user)) {
                ArticleView view = new ArticleView();
                view.setArticle(article);
                view.setViewer(user);
                view.setViewTimestamp(new Date());
                articleViewRepository.save(view);
            }
        }
    }

    @Override
    public void commentArticle(Long articleId, Long userId, Long idCommentReplied, CommentArticleRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        User commenter = userRepository.findById(userId).orElseThrow();
        Comment newComment = new Comment();
        newComment.setArticle(article);
        newComment.setCommenter(commenter);
        newComment.setCommentText(request.getComment());
        newComment.setCommentTimestamp(new Date());

        if (idCommentReplied != 0) {
            Comment commentReplied = commentRepository.findById(idCommentReplied).orElseThrow();
            if (commentReplied.getParentComment() == null) {
                newComment.setParentComment(commentReplied);
            } else {
                newComment.setParentComment(commentReplied.getParentComment());
                newComment.setUsernameTag(commentReplied.getCommenter().getName());
            }
        }
        commentRepository.save(newComment);

        if (idCommentReplied != 0) {
            Comment commentReplied = commentRepository.findById(idCommentReplied).orElseThrow();

            Notification newNotification = new Notification();
            newNotification.setUser(commentReplied.getCommenter());
            newNotification.setType("reply-comment");
            newNotification.setReplyComment(newComment);
            newNotification.setTimestamp(newComment.getCommentTimestamp());
            newNotification.setIsNew(1);
            notificationRepository.save(newNotification);
        }
    }

    @Override
    public void handleLikeComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        Like checkLikeComment = likeRepository.findByCommentAndLiker(comment, user);
        if (checkLikeComment != null) {
            List<Notification> notificationList = notificationRepository.findByLikeComment(checkLikeComment);
            notificationRepository.deleteAll(notificationList);
            likeRepository.deleteById(checkLikeComment.getId());
        } else {
            Like newLikeComment = new Like();
            newLikeComment.setComment(comment);
            newLikeComment.setLiker(user);
            newLikeComment.setLikeTimestamp(new Date());
            likeRepository.save(newLikeComment);

            Notification newNotification = new Notification();
            newNotification.setUser(comment.getCommenter());
            newNotification.setType("like-comment");
            newNotification.setLikeComment(newLikeComment);
            newNotification.setTimestamp(newLikeComment.getLikeTimestamp());
            newNotification.setIsNew(1);
            notificationRepository.save(newNotification);
        }
    }

    @Override
    public GetListArticlesResponse getListSavedArticles(Long userId, PaginationRequest request) {
        User user = userRepository.findById(userId).orElseThrow();

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
        List<ArticleSave> articleSaveList = articleSaveRepository.findByUserOrderBySaveTimestampDesc(user, pageable);
        List<Article> response = new ArrayList<>();
        articleSaveList.forEach(articleSave -> response.add(articleSave.getArticle()));
        int numArticle = articleSaveRepository.countByUser(user);
        return GetListArticlesResponse.builder()
                .listArticle(response)
                .numArticle(numArticle)
                .build();
    }
    @Override
    public GetListArticleViewedResponse getListViewedArticles(Long userId, PaginationRequest request) {
        User user = userRepository.findById(userId).orElseThrow();

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
        List<ArticleView> articleViewList = articleViewRepository.findByViewerOrderByViewTimestampDesc(user, pageable);
        List<ArticleViewedInfoResponse> response = new ArrayList<>();
        articleViewList.forEach(articleView -> {
            ArticleViewedInfoResponse articleViewedInfo = new ArticleViewedInfoResponse();

            Article article = articleView.getArticle();
            articleViewedInfo.setArticle(article);
            if (articleSaveRepository.existsByUserAndArticle(user, article)) {
                articleViewedInfo.setIsUserSaved(1);
            } else {
                articleViewedInfo.setIsUserSaved(0);
            }
            response.add(articleViewedInfo);
        });

        int numArticle = articleViewRepository.countByViewer(user);

        return GetListArticleViewedResponse.builder()
                .listArticle(response)
                .numArticle(numArticle)
                .build();
    }

    @Override
    public void unSaveArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        ArticleSave articleSave = articleSaveRepository.findByUserAndArticle(user, article);
        articleSaveRepository.deleteById(articleSave.getId());
    }
}
