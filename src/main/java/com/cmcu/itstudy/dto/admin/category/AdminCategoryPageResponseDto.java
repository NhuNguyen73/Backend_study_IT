package com.cmcu.itstudy.dto.admin.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryPageResponseDto {

    private List<AdminCategoryResponseDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
