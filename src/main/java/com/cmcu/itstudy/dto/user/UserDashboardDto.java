package com.cmcu.itstudy.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDashboardDto {

    private long totalDocumentsLearned;

    private long totalQuizzesDone;

    private double averageScore;

    /** Percent change vs previous calendar month (average quiz score). */
    private double progressPercent;

    @Builder.Default
    private List<DashboardProgressPointDto> progressHistory = new ArrayList<>();
}
