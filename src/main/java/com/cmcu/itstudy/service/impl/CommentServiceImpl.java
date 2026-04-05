package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.document.DocumentDetailCommentItemDto;
import com.cmcu.itstudy.dto.document.DocumentDetailCommentsDto;
import com.cmcu.itstudy.entity.DocumentComment;
import com.cmcu.itstudy.mapper.DocumentMapper;
import com.cmcu.itstudy.repository.DocumentCommentRepository;
import com.cmcu.itstudy.service.contract.CommentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final DocumentCommentRepository documentCommentRepository;

    public CommentServiceImpl(DocumentCommentRepository documentCommentRepository) {
        this.documentCommentRepository = documentCommentRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public DocumentDetailCommentsDto loadCommentsForDocument(UUID documentId) {
        long total = documentCommentRepository.countByDocumentId(documentId);

        List<DocumentComment> pinnedRows = documentCommentRepository.findPinnedWithAuthor(documentId, PageRequest.of(0, 1));
        DocumentDetailCommentItemDto pinned = pinnedRows.isEmpty()
                ? null
                : DocumentMapper.toCommentItemDto(pinnedRows.get(0));

        List<DocumentComment> latest = documentCommentRepository
                .findTop5ByDocumentIdOrderByCreatedAtDesc(documentId, PageRequest.of(0, 5));
        List<DocumentDetailCommentItemDto> latestDtos = latest.stream()
                .map(DocumentMapper::toCommentItemDto)
                .collect(Collectors.toList());

        return DocumentDetailCommentsDto.builder()
                .totalComments(total)
                .pinnedComment(pinned)
                .latestComments(latestDtos)
                .build();
    }
}
