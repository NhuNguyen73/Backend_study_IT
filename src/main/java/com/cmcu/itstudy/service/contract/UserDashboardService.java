package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.user.UserDashboardDto;

import java.util.UUID;

public interface UserDashboardService {

    UserDashboardDto getDashboardForUser(UUID userId);
}
