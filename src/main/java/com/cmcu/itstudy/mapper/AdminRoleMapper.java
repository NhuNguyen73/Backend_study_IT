package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.admin.role.AdminRoleResponseDto;
import com.cmcu.itstudy.entity.Role;

public final class AdminRoleMapper {

    private AdminRoleMapper() {
    }

    public static AdminRoleResponseDto toResponseDto(Role role) {
        if (role == null) {
            return null;
        }
        boolean active = !Boolean.FALSE.equals(role.getActive());
        return AdminRoleResponseDto.builder()
                .id(role.getId() != null ? role.getId().toString() : null)
                .name(role.getName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .active(active)
                .build();
    }
}
