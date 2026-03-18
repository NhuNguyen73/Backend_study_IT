package com.cmcu.itstudy.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCardResponseDto {
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
    private List<String> tags;
}
