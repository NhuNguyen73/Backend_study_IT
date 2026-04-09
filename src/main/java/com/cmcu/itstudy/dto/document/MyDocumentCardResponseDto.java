package com.cmcu.itstudy.dto.document;

import com.cmcu.itstudy.enums.DocumentStatus;
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
public class MyDocumentCardResponseDto {
    private String id;
    private String title;
    private String thumbnailUrl;
    private String categoryName;
    private String authorName;
    private LocalDateTime createdAt;
    private DocumentStatus status;
    private String fileType;
    private String fileName;
    private Long fileSizeBytes;
    private List<String> tags;
    private String documentUrl;
}
