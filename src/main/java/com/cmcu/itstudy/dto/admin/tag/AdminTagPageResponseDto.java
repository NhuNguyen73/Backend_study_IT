package com.cmcu.itstudy.dto.admin.tag;

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
public class AdminTagPageResponseDto {

    private List<AdminTagResponseDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
