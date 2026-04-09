package com.cmcu.itstudy.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private String id;
    private String body;
    private String authorName;
    private String authorAvatar;
    private Integer likeCount;
    private Boolean isLiked;
    private Integer replyCount;
    private String replyToUserName;
    private LocalDateTime createdAt;
}
