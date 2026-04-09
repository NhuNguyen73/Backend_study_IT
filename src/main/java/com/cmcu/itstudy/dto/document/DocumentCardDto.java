package com.cmcu.itstudy.dto.document;

import com.cmcu.itstudy.enums.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCardDto {

    private String id;
    private String title;
    private String slug;
    private String description;
    private String thumbnailUrl;
    private String fileName;
    private String fileType; // Use enum name as String
    private Long fileSize;
    private DocumentStatus status;
    private LocalDateTime uploadDate;
    private Long views;
    private Long downloads;
    private Long bookmarks;

    // Added fields to match frontend expectations and service implementation
    private String categoryName;
    private String authorName;
    private List<String> tags;
    private String documentUrl;
}
