package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.document.DocumentCardResponseDto;
import com.cmcu.itstudy.dto.document.HomepageStatisticsResponseDto;

import java.util.List;
import java.util.UUID;

public interface HomepageService {

    HomepageStatisticsResponseDto getStatistics();

    List<DocumentCardResponseDto> getLatestDocuments(int limit, UUID currentUserId);

    List<DocumentCardResponseDto> getTrendingDocuments(int limit, UUID currentUserId);
}

