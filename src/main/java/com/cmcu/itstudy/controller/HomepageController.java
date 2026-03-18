package com.cmcu.itstudy.controller;

import com.cmcu.itstudy.dto.common.ApiResponse;
import com.cmcu.itstudy.dto.document.DocumentCardResponseDto;
import com.cmcu.itstudy.dto.document.HomepageStatisticsResponseDto;
import com.cmcu.itstudy.service.contract.HomepageService;
import com.cmcu.itstudy.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/homepage")
public class HomepageController {

    private final HomepageService homepageService;

    public HomepageController(HomepageService homepageService) {
        this.homepageService = homepageService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<HomepageStatisticsResponseDto>> getStatistics() {
        HomepageStatisticsResponseDto data = homepageService.getStatistics();
        return ResponseEntity.ok(ApiResponse.success(data, "Homepage statistics"));
    }

    @GetMapping("/latest-documents")
    public ResponseEntity<ApiResponse<List<DocumentCardResponseDto>>> getLatestDocuments(
            @RequestParam(name = "limit", defaultValue = "4") int limit,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        UUID currentUserId = currentUser != null ? currentUser.getUser().getId() : null;
        List<DocumentCardResponseDto> data = homepageService.getLatestDocuments(limit, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(data, "Latest documents"));
    }

    @GetMapping("/trending-documents")
    public ResponseEntity<ApiResponse<List<DocumentCardResponseDto>>> getTrendingDocuments(
            @RequestParam(name = "limit", defaultValue = "5") int limit,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        UUID currentUserId = currentUser != null ? currentUser.getUser().getId() : null;
        List<DocumentCardResponseDto> data = homepageService.getTrendingDocuments(limit, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(data, "Trending documents"));
    }
}

