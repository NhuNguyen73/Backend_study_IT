package com.cmcu.itstudy.dto.user;

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
public class DashboardProgressPointDto {

    /** ISO date yyyy-MM-dd */
    private String date;

    private double score;
}
