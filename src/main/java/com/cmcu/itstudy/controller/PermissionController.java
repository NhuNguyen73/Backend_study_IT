package com.cmcu.itstudy.controller;

import com.cmcu.itstudy.dto.admin.permission.AdminPermissionResponseDto;
import com.cmcu.itstudy.dto.common.ApiResponse;
import com.cmcu.itstudy.service.contract.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MODERATOR')")
    public ResponseEntity<ApiResponse<List<AdminPermissionResponseDto>>> listPermissions() {
        List<AdminPermissionResponseDto> data = permissionService.listAllPermissions();
        return ResponseEntity.ok(ApiResponse.success(data, "Permission list"));
    }
}
