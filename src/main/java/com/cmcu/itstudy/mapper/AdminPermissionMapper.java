package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.admin.permission.AdminPermissionResponseDto;
import com.cmcu.itstudy.entity.Permission;

public final class AdminPermissionMapper {

    private AdminPermissionMapper() {
    }

    public static AdminPermissionResponseDto toDto(Permission p) {
        if (p == null) {
            return null;
        }
        return AdminPermissionResponseDto.builder()
                .id(p.getId() != null ? p.getId().toString() : null)
                .name(p.getName())
                .description(p.getDescription())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
