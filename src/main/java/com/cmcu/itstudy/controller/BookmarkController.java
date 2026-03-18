package com.cmcu.itstudy.controller;

import com.cmcu.itstudy.dto.common.ApiResponse;
import com.cmcu.itstudy.dto.common.MessageResponseDto;
import com.cmcu.itstudy.dto.document.PagedResponseDocumentCardDto;
import com.cmcu.itstudy.security.UserDetailsImpl;
import com.cmcu.itstudy.service.contract.DocumentService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BookmarkController {

    private final DocumentService documentService;

    public BookmarkController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/bookmarks/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MessageResponseDto>> addBookmark(
            @PathVariable("id") UUID documentId,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        UUID userId = currentUser.getUser().getId();
        documentService.toggleBookmark(documentId, userId);
        MessageResponseDto body = MessageResponseDto.builder()
                .message("Bookmark toggled")
                .build();
        return ResponseEntity.ok(ApiResponse.success(body, body.getMessage()));
    }

    @DeleteMapping("/bookmarks/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MessageResponseDto>> removeBookmark(
            @PathVariable("id") UUID documentId,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        UUID userId = currentUser.getUser().getId();
        documentService.toggleBookmark(documentId, userId);
        MessageResponseDto body = MessageResponseDto.builder()
                .message("Bookmark toggled")
                .build();
        return ResponseEntity.ok(ApiResponse.success(body, body.getMessage()));
    }

    @GetMapping("/bookmarks/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PagedResponseDocumentCardDto>> getMyBookmarks(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size
    ) {
        UUID userId = currentUser.getUser().getId();
        // Service trả về list, ở đây wrap lại paging đơn giản cho khớp OpenAPI
        var list = documentService.getMyBookmarks(userId);
        int fromIndex = Math.min(page * size, list.size());
        int toIndex = Math.min(fromIndex + size, list.size());
        var pageContent = list.subList(fromIndex, toIndex);

        PagedResponseDocumentCardDto data = PagedResponseDocumentCardDto.builder()
                .content(pageContent)
                .page(page)
                .size(size)
                .totalElements(list.size())
                .totalPages((int) Math.ceil((double) list.size() / size))
                .build();

        return ResponseEntity.ok(ApiResponse.success(data, "My bookmarks"));
    }
}

