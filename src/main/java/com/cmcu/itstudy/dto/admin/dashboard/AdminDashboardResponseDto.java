package com.cmcu.itstudy.dto.admin.dashboard;

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
public class AdminDashboardResponseDto {

    private long totalUsers;

    private double userGrowthPercent;

    private long totalDocuments;

    private double documentGrowthPercent;

    private long pendingRequests;

    @Builder.Default
    private List<AdminDashboardActiveDayDto> activeUsersByDay = new ArrayList<>();

    @Builder.Default
    private List<AdminDashboardLatestUserDto> latestUsers = new ArrayList<>();
}
