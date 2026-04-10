package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.document.CommentResponse;
import com.cmcu.itstudy.entity.DocumentComment;

import java.util.UUID;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponse toCommentResponse(DocumentComment comment, Boolean isLiked, Integer replyCount) {
        if (comment == null) {
            return null;
        }
        String authorName = comment.getAuthor() != null ? comment.getAuthor().getFullName() : null;
        String authorAvatar = comment.getAuthor() != null ? comment.getAuthor().getAvatarUrl() : null;
        String replyToUserName = comment.getReplyToUser() != null ? comment.getReplyToUser().getFullName() : null;
        return CommentResponse.builder()
                .id(uuidToString(comment.getId()))
                .body(comment.getBody())
                .authorName(authorName)
                .authorAvatar(authorAvatar)
                .likeCount(comment.getLikeCount())
                .isLiked(isLiked)
                .replyCount(replyCount)
                .replyToUserName(replyToUserName)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private static String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }
}
