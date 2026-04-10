package com.cmcu.itstudy.controller;

import com.cmcu.itstudy.dto.admin.dashboard.AdminDashboardResponseDto;
import com.cmcu.itstudy.dto.common.ApiResponse;
import com.cmcu.itstudy.service.contract.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardResponseDto>> getDashboard() {
        AdminDashboardResponseDto data = adminDashboardService.getDashboard();
        return ResponseEntity.ok(ApiResponse.success(data, "OK"));
    }
}
