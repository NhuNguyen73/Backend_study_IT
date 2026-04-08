package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.document.DocumentDetailResponseDto;
import com.cmcu.itstudy.dto.document.DocumentFileUrlResponseDto;

import java.util.UUID;

public interface DocumentQueryService {

    DocumentDetailResponseDto getDocumentDetail(UUID id, UUID currentUserId);

    DocumentFileUrlResponseDto getDocumentPrimaryFileUrl(UUID documentId);
}
