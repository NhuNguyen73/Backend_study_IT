package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.document.DocumentCardResponseDto;
import com.cmcu.itstudy.dto.document.DocumentDetailQuizDto;
import com.cmcu.itstudy.dto.document.DocumentDetailResponseDto;
import com.cmcu.itstudy.dto.document.DocumentFileUrlResponseDto;
import com.cmcu.itstudy.dto.document.DocumentPrimaryFileDto;
import com.cmcu.itstudy.dto.document.DocumentRelatedDocumentDto;
import com.cmcu.itstudy.dto.document.QuizListPageResponseDto;
import com.cmcu.itstudy.entity.Document;
import com.cmcu.itstudy.mapper.DocumentMapper;
import com.cmcu.itstudy.repository.DocumentDownloadRepository;
import com.cmcu.itstudy.repository.DocumentFileRepository;
import com.cmcu.itstudy.repository.DocumentViewRepository;
import com.cmcu.itstudy.service.contract.CommentService;
import com.cmcu.itstudy.service.contract.DocumentQueryService;
import com.cmcu.itstudy.service.contract.DocumentService;
import com.cmcu.itstudy.service.contract.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentQueryServiceImpl implements DocumentQueryService {

    private static final int RELATED_LIMIT = 5;

    private final DocumentService documentService;
    private final CommentService commentService;
    private final QuizService quizService;
    private final DocumentFileRepository documentFileRepository;
    private final DocumentViewRepository documentViewRepository;
    private final DocumentDownloadRepository documentDownloadRepository;
    private final DocumentCardEnrichmentService documentCardEnrichmentService;

    public DocumentQueryServiceImpl(DocumentService documentService,
                                    CommentService commentService,
                                    QuizService quizService,
                                    DocumentFileRepository documentFileRepository,
                                    DocumentViewRepository documentViewRepository,
                                    DocumentDownloadRepository documentDownloadRepository,
                                    DocumentCardEnrichmentService documentCardEnrichmentService) {
        this.documentService = documentService;
        this.commentService = commentService;
        this.quizService = quizService;
        this.documentFileRepository = documentFileRepository;
        this.documentViewRepository = documentViewRepository;
        this.documentDownloadRepository = documentDownloadRepository;
        this.documentCardEnrichmentService = documentCardEnrichmentService;
    }

    @Transactional(readOnly = true)
    @Override
    public DocumentDetailResponseDto getDocumentDetail(UUID id, UUID currentUserId) {
        Document document = documentService.getById(id);

        DocumentPrimaryFileDto primaryFile = documentFileRepository.findByDocumentIdAndPrimaryTrue(id)
                .map(DocumentMapper::toPrimaryFileDto)
                .orElse(null);

        long totalViews = documentViewRepository.countByDocumentId(id);
        long totalDownloads = documentDownloadRepository.countByDocumentId(id);

        var comments = commentService.loadCommentsForDocument(id);
        List<DocumentDetailQuizDto> quizzes = quizService.loadQuizzesForDocument(id);

        List<DocumentRelatedDocumentDto> related = documentService.getRelatedDocuments(id, RELATED_LIMIT).stream()
                .map(DocumentMapper::toRelatedDocumentDto)
                .collect(Collectors.toList());

        List<DocumentCardResponseDto> cards = documentCardEnrichmentService
                .toEnrichedCardDtos(List.of(document), currentUserId);
        String authorName = cards.isEmpty() ? null : cards.get(0).getAuthorName();
        List<String> tags = Collections.emptyList();
        if (!cards.isEmpty() && cards.get(0).getTags() != null) {
            tags = cards.get(0).getTags();
        }

        return DocumentMapper.toDetailResponseDto(
                document,
                authorName,
                tags,
                totalViews,
                totalDownloads,
                primaryFile,
                comments,
                quizzes,
                related
        );
    }

    @Transactional(readOnly = true)
    @Override
    public DocumentFileUrlResponseDto getDocumentPrimaryFileUrl(UUID documentId) {
        documentService.getById(documentId);

        return documentFileRepository.findByDocumentIdAndPrimaryTrue(documentId)
                .map(DocumentMapper::toFileUrlResponseDto)
                .orElseThrow(() -> new NoSuchElementException("Primary document file not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public QuizListPageResponseDto getQuizzesByDocument(UUID documentId, int page, int size) {
        return quizService.getQuizzesByDocument(documentId, page, size);
    }
}
