package com.example.skyNews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentArticleResponse {
    private CommentInfoResponse parentComment;
    private List<CommentInfoResponse> childComments;
}
