package com.cmcu.itstudy.dto.admin.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardLatestUserDto {

    private UUID id;

    private String name;

    /** yyyy-MM-dd */
    private String createdAt;
}
