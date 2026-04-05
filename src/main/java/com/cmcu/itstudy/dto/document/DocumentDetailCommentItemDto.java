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
public class DocumentDetailCommentItemDto {
    private String id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
    private Integer totalLikes;
    private Boolean isPinned;
}
