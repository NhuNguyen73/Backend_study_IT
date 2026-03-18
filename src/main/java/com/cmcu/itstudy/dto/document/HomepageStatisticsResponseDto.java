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
public class HomepageStatisticsResponseDto {
    private Integer totalApprovedDocuments;
    private Integer totalActiveUsers;
    private Integer totalDownloads;
    private Integer totalContributors;
}
