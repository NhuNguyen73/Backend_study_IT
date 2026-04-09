package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.document.DocumentDetailResponseDto;
import com.cmcu.itstudy.dto.document.DocumentFileUrlResponseDto;
import com.cmcu.itstudy.dto.document.QuizListPageResponseDto;

import java.util.UUID;

public interface DocumentQueryService {

    DocumentDetailResponseDto getDocumentDetail(UUID id, UUID currentUserId);

    DocumentFileUrlResponseDto getDocumentPrimaryFileUrl(UUID documentId);

    QuizListPageResponseDto getQuizzesByDocument(UUID documentId, int page, int size);
}
