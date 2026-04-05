package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.entity.Document;
import com.cmcu.itstudy.enums.DocumentStatus;
import com.cmcu.itstudy.repository.DocumentRepository;
import com.cmcu.itstudy.service.contract.DocumentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Document getById(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Document not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Document> getRelatedDocuments(UUID documentId, int limit) {
        Document doc = getById(documentId);
        if (doc.getCategory() == null || doc.getCategory().getId() == null) {
            return Collections.emptyList();
        }
        Slice<Document> slice = documentRepository.findRelatedDocumentsForDetail(
                DocumentStatus.APPROVED,
                doc.getCategory().getId(),
                documentId,
                PageRequest.of(0, Math.max(1, limit)));
        return slice.getContent();
    }
}
