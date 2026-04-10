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
public class MyDocumentDetailDto {

    private String id;
    private String title;
    private String description;
    private String documentUrl;
    private String thumbnailUrl;
    private String fileName;
    private String fileType;
    private Long fileSizeBytes;
    private String categoryName;
    private List<String> tags;
    private DocumentStatus status;
    private String rejectReason;
    private LocalDateTime createdAt;
}
