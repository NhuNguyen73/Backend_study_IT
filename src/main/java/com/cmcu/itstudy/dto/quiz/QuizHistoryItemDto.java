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
    private Integer attemptNumber;
    private LocalDateTime startTime;
    private Double score;
    private Double maxScore;
    private Double scorePercent;
    private LocalDateTime attemptDate;
    private String status;
    /** Total seconds between start and end; null if attempt not submitted. */
    private Long totalTimeSpentSeconds;
    private Double rankingPercent;
    private String rankingLabel;
    private Double progressPercent;
    private String level;
}
