package com.cmcu.itstudy.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizListItemDto {

    private String quizId;
    private String title;
    private String description;
    private Long totalQuestions;
    private Integer durationMinutes;
    private Double passScorePercent;
}
