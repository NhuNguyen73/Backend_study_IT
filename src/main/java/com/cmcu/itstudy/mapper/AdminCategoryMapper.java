package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.admin.category.AdminCategoryResponseDto;
import com.cmcu.itstudy.entity.Category;

public final class AdminCategoryMapper {

    private AdminCategoryMapper() {
    }

    public static AdminCategoryResponseDto toDto(Category c) {
        if (c == null) {
            return null;
        }
        String parentId = c.getParent() != null && c.getParent().getId() != null
                ? c.getParent().getId().toString()
                : null;
        return AdminCategoryResponseDto.builder()
                .id(c.getId() != null ? c.getId().toString() : null)
                .name(c.getName())
                .slug(c.getSlug())
                .description(c.getDescription())
                .parentId(parentId)
                .active(c.getActive())
                .displayOrder(c.getDisplayOrder())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
