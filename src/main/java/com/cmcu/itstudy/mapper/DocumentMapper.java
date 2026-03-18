package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.document.DocumentCardResponseDto;
import com.cmcu.itstudy.dto.document.DocumentDetailResponseDto;
import com.cmcu.itstudy.dto.document.DocumentStatusDto;
import com.cmcu.itstudy.dto.document.FileTypeDto;
import com.cmcu.itstudy.entity.Document;
import com.cmcu.itstudy.enums.DocumentStatus;
import com.cmcu.itstudy.enums.FileType;

public final class DocumentMapper {

    private DocumentMapper() {
    }

    public static DocumentCardResponseDto toCardDto(Document document) {
        if (document == null) {
            return null;
        }

        return DocumentCardResponseDto.builder()
                .id(uuidToString(document.getId()))
                .title(document.getTitle())
                .thumbnail(document.getThumbnailUrl())
                .categoryName(document.getCategory() != null ? document.getCategory().getName() : null)
                .authorName(null)
                .createdAt(document.getCreatedAt())
                .viewCount(document.getViewCount() != null ? document.getViewCount().intValue() : null)
                .downloadCount(document.getDownloadCount() != null ? document.getDownloadCount().intValue() : null)
                .isBookmarked(null)
                .fileType(map(document.getFileType()))
                .tags(null)
                .build();
    }

    public static DocumentDetailResponseDto toDetailDto(Document document) {
        if (document == null) {
            return null;
        }

        return DocumentDetailResponseDto.builder()
                .id(uuidToString(document.getId()))
                .title(document.getTitle())
                .thumbnail(document.getThumbnailUrl())
                .categoryName(document.getCategory() != null ? document.getCategory().getName() : null)
                .authorName(null)
                .createdAt(document.getCreatedAt())
                .viewCount(document.getViewCount() != null ? document.getViewCount().intValue() : null)
                .downloadCount(document.getDownloadCount() != null ? document.getDownloadCount().intValue() : null)
                .isBookmarked(null)
                .fileType(map(document.getFileType()))
                .tags(null)
                .description(document.getDescription())
                .fileUrl(document.getFileUrl())
                .fileSize(document.getFileSize())
                .status(map(document.getStatus()))
                .isDeleted(document.getDeleted() != null ? document.getDeleted() : Boolean.FALSE)
                .deletedAt(document.getDeletedAt())
                .deletedBy(uuidToString(document.getDeletedBy() != null ? document.getDeletedBy().getId() : null))
                .build();
    }

    private static String uuidToString(java.util.UUID id) {
        return id != null ? id.toString() : null;
    }

    private static FileTypeDto map(FileType fileType) {
        if (fileType == null) {
            return null;
        }
        return switch (fileType) {
            case PPT -> FileTypeDto.PPTX;
            default -> FileTypeDto.valueOf(fileType.name());
        };
    }

    private static DocumentStatusDto map(DocumentStatus status) {
        return status != null ? DocumentStatusDto.valueOf(status.name()) : null;
    }
}
