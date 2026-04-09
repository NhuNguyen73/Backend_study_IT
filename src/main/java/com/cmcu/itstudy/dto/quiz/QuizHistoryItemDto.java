package com.cmcu.itstudy.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizHistoryItemDto {

    private String attemptId;
    private String quizId;
    private String quizTitle;
    private Double score;
    private Double scorePercent;
    private LocalDateTime attemptDate;
    private String status;
}
