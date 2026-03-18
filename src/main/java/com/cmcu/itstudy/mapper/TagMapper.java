package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.document.TagResponseDto;
import com.cmcu.itstudy.entity.Tag;

public final class TagMapper {

    private TagMapper() {
    }

    public static TagResponseDto toDto(Tag tag) {
        if (tag == null) {
            return null;
        }

        return TagResponseDto.builder()
                .id(tag.getId() != null ? tag.getId().toString() : null)
                .name(tag.getName())
                .build();
    }
}
