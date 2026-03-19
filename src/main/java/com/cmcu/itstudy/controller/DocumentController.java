package com.cmcu.itstudy.controller;

import com.cmcu.itstudy.dto.common.ApiResponse;
import com.cmcu.itstudy.dto.common.MessageResponseDto;
import com.cmcu.itstudy.dto.document.DocumentDetailResponseDto;
import com.cmcu.itstudy.dto.document.DocumentListRequestDto;
import com.cmcu.itstudy.dto.document.PagedResponseDocumentCardDto;
import com.cmcu.itstudy.security.UserDetailsImpl;
import com.cmcu.itstudy.service.contract.DocumentService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    private static final int KEYWORD_MAX_LENGTH = 50;

    @GetMapping("/documents")
    public ResponseEntity<ApiResponse<PagedResponseDocumentCardDto>> getDocuments(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "categoryId", required = false) String categoryId,
            @RequestParam(name = "tagIds", required = false) List<String> tagIds,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        String normalizedKeyword = (keyword != null) ? keyword.trim() : null;
        if (normalizedKeyword != null && normalizedKeyword.length() > KEYWORD_MAX_LENGTH) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm tối đa " + KEYWORD_MAX_LENGTH + " ký tự.");
        }
        DocumentListRequestDto request = DocumentListRequestDto.builder()
                .keyword(normalizedKeyword)
                .categoryId(categoryId)
                .tagIds(tagIds)
                .sort(sort)
                .page(page)
                .size(size)
                .build();

        UUID currentUserId = currentUser != null ? currentUser.getUser().getId() : null;
        PagedResponseDocumentCardDto data = documentService.searchDocuments(request, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(data, "Document list"));
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<ApiResponse<DocumentDetailResponseDto>> getDocumentDetail(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        UUID currentUserId = currentUser != null ? currentUser.getUser().getId() : null;
        DocumentDetailResponseDto data = documentService.getDocumentDetail(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(data, "Document detail"));
    }

    @PostMapping("/documents/{id}/view")
    public ResponseEntity<ApiResponse<MessageResponseDto>> increaseView(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        UUID currentUserId = currentUser != null ? currentUser.getUser().getId() : null;
        documentService.increaseView(id, currentUserId);
        MessageResponseDto body = MessageResponseDto.builder()
                .message("View recorded")
                .build();
        return ResponseEntity.ok(ApiResponse.success(body, body.getMessage()));
    }

    @PostMapping("/documents/{id}/download")
    public ResponseEntity<ApiResponse<MessageResponseDto>> downloadDocument(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        UUID currentUserId = currentUser != null ? currentUser.getUser().getId() : null;
        documentService.downloadDocument(id, currentUserId);
        MessageResponseDto body = MessageResponseDto.builder()
                .message("Download recorded")
                .build();
        return ResponseEntity.ok(ApiResponse.success(body, body.getMessage()));
    }
}

