package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.admin.document.AdminPendingDocumentsPageResponseDto;
import com.cmcu.itstudy.dto.admin.document.DocumentAdminDetailDto;
import com.cmcu.itstudy.dto.admin.document.DocumentAdminStatusPatchRequestDto;
import com.cmcu.itstudy.entity.User;

import java.util.UUID;

public interface AdminDocumentService {

    AdminPendingDocumentsPageResponseDto listPendingDocuments(int page, int size);

    DocumentAdminDetailDto getDocumentDetail(UUID documentId);

    void updateDocumentStatus(UUID documentId, DocumentAdminStatusPatchRequestDto request, User moderator);
}
