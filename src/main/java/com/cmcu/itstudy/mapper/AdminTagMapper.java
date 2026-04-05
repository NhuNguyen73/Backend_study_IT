package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.admin.tag.AdminTagResponseDto;
import com.cmcu.itstudy.entity.Tag;

public final class AdminTagMapper {

    private AdminTagMapper() {
    }

    public static AdminTagResponseDto toDto(Tag t) {
        if (t == null) {
            return null;
        }
        return AdminTagResponseDto.builder()
                .id(t.getId() != null ? t.getId().toString() : null)
                .name(t.getName())
                .slug(t.getSlug())
                .usageCount(t.getUsageCount())
                .active(t.getActive())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}
