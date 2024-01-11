package com.example.skyNews.dto.response;

import com.example.skyNews.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentInfoResponse {
    private Long id;
    private User commenter;
    private String commentText;
    private Date commentTimestamp;
    private String usernameTag;
    private Integer numLike;
    private Integer isUserLike;
}
