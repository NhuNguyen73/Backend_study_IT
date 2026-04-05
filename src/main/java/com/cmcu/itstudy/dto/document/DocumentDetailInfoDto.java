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
public class DocumentDetailInfoDto {
    private String id;
    private String title;
    private String description;
    private FileTypeDto documentType;
    private LocalDateTime createdAt;
    private String authorName;
    private String categoryName;
    private List<String> tags;
}
