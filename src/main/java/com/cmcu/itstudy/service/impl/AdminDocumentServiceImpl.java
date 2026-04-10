package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.admin.document.AdminPendingDocumentsPageResponseDto;
import com.cmcu.itstudy.dto.admin.document.DocumentAdminDetailDto;
import com.cmcu.itstudy.dto.admin.document.DocumentAdminStatusPatchRequestDto;
import com.cmcu.itstudy.dto.document.DocumentCardDto;
import com.cmcu.itstudy.entity.Document;
import com.cmcu.itstudy.entity.DocumentFile;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.enums.DocumentStatus;
import com.cmcu.itstudy.repository.DocumentFileRepository;
import com.cmcu.itstudy.repository.DocumentRepository;
import com.cmcu.itstudy.service.contract.AdminDocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminDocumentServiceImpl implements AdminDocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentFileRepository documentFileRepository;

    public AdminDocumentServiceImpl(DocumentRepository documentRepository,
                                    DocumentFileRepository documentFileRepository) {
        this.documentRepository = documentRepository;
        this.documentFileRepository = documentFileRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentAdminDetailDto getDocumentDetail(UUID documentId) {
        Document document = documentRepository.findByIdAndDeletedFalse(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + documentId));
        String previewUrl = resolvePreviewFileUrl(document);
        String storagePath = documentFileRepository.findByDocumentIdAndPrimaryTrue(documentId)
                .map(DocumentFile::getStoragePath)
                .orElse(null);
        return DocumentAdminDetailDto.builder()
                .id(document.getId() != null ? document.getId().toString() : null)
                .title(document.getTitle())
                .description(document.getDescription())
                .fileUrl(previewUrl)
                .thumbnailUrl(document.getThumbnailUrl())
                .fileType(document.getFileType() != null ? document.getFileType().name() : null)
                .fileName(document.getFileName())
                .fileSizeBytes(document.getFileSize())
                .authorName(document.getCreatedBy() != null ? document.getCreatedBy().getFullName() : null)
                .categoryName(document.getCategory() != null ? document.getCategory().getName() : null)
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .rejectReason(document.getRejectReason())
                .storagePath(storagePath)
                .build();
    }

    /**
     * Ưu tiên URL công khai từ DocumentFile (fileUrl), sau đó storagePath nếu là URL đầy đủ,
     * cuối cùng {@link Document#getFileUrl()} (Supabase public URL từ luồng upload).
     */
    private String resolvePreviewFileUrl(Document document) {
        return documentFileRepository.findByDocumentIdAndPrimaryTrue(document.getId())
                .map(this::previewUrlFromPrimaryFile)
                .filter(StringUtils::hasText)
                .orElseGet(() -> StringUtils.hasText(document.getFileUrl()) ? document.getFileUrl().trim() : null);
    }

    private String previewUrlFromPrimaryFile(DocumentFile file) {
        if (StringUtils.hasText(file.getFileUrl()) && isHttpUrl(file.getFileUrl())) {
            return file.getFileUrl().trim();
        }
        if (StringUtils.hasText(file.getStoragePath()) && isHttpUrl(file.getStoragePath())) {
            return file.getStoragePath().trim();
        }
        return null;
    }

    private static boolean isHttpUrl(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        String v = value.trim().toLowerCase();
        return v.startsWith("https://") || v.startsWith("http://");
    }

    @Override
    @Transactional(readOnly = true)
    public AdminPendingDocumentsPageResponseDto listPendingDocuments(int page, int size) {
        int p = Math.max(0, page);
        int s = size < 1 ? 10 : Math.min(size, 100);
        Page<Document> result = documentRepository.findPendingPageWithCategoryAndCreator(
                DocumentStatus.PENDING,
                PageRequest.of(p, s)
        );
        List<DocumentCardDto> content = result.getContent().stream()
                .map(this::toPendingCardDto)
                .collect(Collectors.toList());
        return AdminPendingDocumentsPageResponseDto.builder()
                .content(content)
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    private DocumentCardDto toPendingCardDto(Document d) {
        return DocumentCardDto.builder()
                .id(d.getId() != null ? d.getId().toString() : null)
                .title(d.getTitle())
                .slug(d.getSlug())
                .description(d.getDescription())
                .thumbnailUrl(d.getThumbnailUrl())
                .fileName(d.getFileName())
                .fileType(d.getFileType() != null ? d.getFileType().name() : null)
                .fileSize(d.getFileSize())
                .status(d.getStatus())
                .uploadDate(d.getCreatedAt())
                .views(d.getViewCount())
                .downloads(d.getDownloadCount())
                .bookmarks(d.getBookmarkCount())
                .categoryName(d.getCategory() != null ? d.getCategory().getName() : null)
                .authorName(d.getCreatedBy() != null ? d.getCreatedBy().getFullName() : null)
                .tags(null)
                .documentUrl(d.getFileUrl())
                .storagePath(null)
                .build();
    }

    @Override
    @Transactional
    public void updateDocumentStatus(UUID documentId, DocumentAdminStatusPatchRequestDto request, User moderator) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + documentId));

        if (Boolean.TRUE.equals(document.getDeleted())) {
            throw new IllegalStateException("Document is deleted");
        }
        if (document.getStatus() != DocumentStatus.PENDING) {
            throw new IllegalStateException("Only PENDING documents can be approved or rejected");
        }

        DocumentStatus target = request.getStatus();
        if (target != DocumentStatus.APPROVED && target != DocumentStatus.REJECTED) {
            throw new IllegalArgumentException("status must be APPROVED or REJECTED");
        }

        if (target == DocumentStatus.REJECTED) {
            if (!StringUtils.hasText(request.getRejectReason())) {
                throw new IllegalArgumentException("rejectReason is required when rejecting");
            }
            document.setRejectReason(request.getRejectReason().trim());
        } else {
            document.setRejectReason(null);
            document.setPublishedAt(LocalDateTime.now());
        }

        document.setStatus(target);
        document.setUpdatedBy(moderator);
        documentRepository.save(document);
    }
}
