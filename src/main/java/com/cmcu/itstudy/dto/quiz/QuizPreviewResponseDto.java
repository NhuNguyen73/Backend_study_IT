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
public class QuizPreviewResponseDto {

    private String quizId;
    private String quizTitle;
    private Long totalQuestions;
    private Integer duration;
    private Double passScorePercent;
    private List<QuizHistoryItemDto> recentAttempts;
}
