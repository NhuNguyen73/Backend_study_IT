package com.cmcu.itstudy.dto.admin;

import com.cmcu.itstudy.enums.ContributorRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateContributorRequestStatusDto {
    private UUID requestId;
    private ContributorRequestStatus status;
    private String rejectionReason;
}
