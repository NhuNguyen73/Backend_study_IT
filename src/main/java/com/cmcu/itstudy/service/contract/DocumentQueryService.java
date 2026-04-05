package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.document.DocumentDetailResponseDto;

import java.util.UUID;

public interface DocumentQueryService {

    DocumentDetailResponseDto getDocumentDetail(UUID id, UUID currentUserId);
}
