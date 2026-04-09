package com.cmcu.itstudy.dto.quiz;

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
public class QuizHistorySummaryDto {

    /**
     * Share of PASSED among finished attempts (PASSED + FAILED), 0–100; null if no finished attempts.
     */
    private Double passRatePercent;

    /**
     * Average raw score among submitted attempts; null if none.
     */
    private Double averageScore;

    /** Total minutes spent on submitted attempts (floor of summed durations). */
    private Long totalTimeSpentMinutes;
}
