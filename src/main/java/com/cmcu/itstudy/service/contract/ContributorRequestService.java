package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.admin.AdminContributorRequestDto;

import java.util.List;

public interface ContributorRequestService {
    List<AdminContributorRequestDto> getAllContributorRequestsForAdmin();
}
