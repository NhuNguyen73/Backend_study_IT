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
public class DocumentDetailResponseDto {
    private String id;
    private String title;
    private String thumbnail;
    private String categoryName;
    private String authorName;
    private LocalDateTime createdAt;
    private Integer viewCount;
    private Integer downloadCount;
    private Boolean isBookmarked;
    private FileTypeDto fileType;
    private java.util.List<String> tags;
    private String description;
    private String fileUrl;
    private Long fileSize;
    private DocumentStatusDto status;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
}
