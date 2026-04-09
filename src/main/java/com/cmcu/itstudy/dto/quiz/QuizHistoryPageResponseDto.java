package com.cmcu.itstudy.dto.quiz;

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
public class QuizHistoryPageResponseDto {

    private List<QuizHistoryItemDto> items;
    private Integer page;
    private Integer totalPages;
    private Long totalItems;
}
