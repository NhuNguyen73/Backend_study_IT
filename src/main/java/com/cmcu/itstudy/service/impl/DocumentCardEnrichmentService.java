package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.document.DocumentCardResponseDto;
import com.cmcu.itstudy.entity.Author;
import com.cmcu.itstudy.entity.Document;
import com.cmcu.itstudy.entity.DocumentAuthor;
import com.cmcu.itstudy.entity.DocumentBookmark;
import com.cmcu.itstudy.entity.DocumentTag;
import com.cmcu.itstudy.entity.Tag;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.mapper.DocumentMapper;
import com.cmcu.itstudy.repository.AuthorRepository;
import com.cmcu.itstudy.repository.DocumentAuthorRepository;
import com.cmcu.itstudy.repository.DocumentBookmarkRepository;
import com.cmcu.itstudy.repository.DocumentTagRepository;
import com.cmcu.itstudy.repository.TagRepository;
import com.cmcu.itstudy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DocumentCardEnrichmentService {

    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final DocumentAuthorRepository documentAuthorRepository;
    private final DocumentTagRepository documentTagRepository;
    private final DocumentBookmarkRepository documentBookmarkRepository;
    private final UserRepository userRepository;

    public DocumentCardEnrichmentService(AuthorRepository authorRepository,
                                         TagRepository tagRepository,
                                         DocumentAuthorRepository documentAuthorRepository,
                                         DocumentTagRepository documentTagRepository,
                                         DocumentBookmarkRepository documentBookmarkRepository,
                                         UserRepository userRepository) {
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.documentAuthorRepository = documentAuthorRepository;
        this.documentTagRepository = documentTagRepository;
        this.documentBookmarkRepository = documentBookmarkRepository;
        this.userRepository = userRepository;
    }

    public List<DocumentCardResponseDto> toEnrichedCardDtos(List<Document> documents, UUID currentUserId) {
        if (documents == null || documents.isEmpty()) {
            return Collections.emptyList();
        }

        Map<UUID, String> authorNames = loadAuthorNames(documents);
        Map<UUID, List<String>> tagsByDocument = loadTagNames(documents);
        Set<UUID> bookmarkedDocumentIds = loadBookmarkedDocumentIds(documents, currentUserId);

        return documents.stream()
                .map(DocumentMapper::toCardDto)
                .peek(dto -> enrichCardDto(dto, authorNames, tagsByDocument, bookmarkedDocumentIds))
                .collect(Collectors.toList());
    }

    private Map<UUID, String> loadAuthorNames(List<Document> documents) {
        Set<UUID> documentIds = documents.stream()
                .map(Document::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (documentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<DocumentAuthor> documentAuthors = documentAuthorRepository.findByDocumentIdIn(documentIds);
        Set<UUID> authorIds = documentAuthors.stream()
                .map(DocumentAuthor::getAuthorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<UUID, Author> authorsById = authorRepository.findAllById(authorIds).stream()
                .collect(Collectors.toMap(Author::getId, Function.identity()));

        Map<UUID, String> result = new HashMap<>();
        for (DocumentAuthor da : documentAuthors) {
            UUID docId = da.getDocumentId();
            UUID authorId = da.getAuthorId();
            Author author = authorsById.get(authorId);
            if (docId != null && author != null && !result.containsKey(docId)) {
                result.put(docId, author.getName());
            }
        }
        return result;
    }

    private Map<UUID, List<String>> loadTagNames(List<Document> documents) {
        Set<UUID> documentIds = documents.stream()
                .map(Document::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (documentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<DocumentTag> documentTags = documentTagRepository.findByDocumentIdIn(documentIds);
        Set<UUID> tagIds = documentTags.stream()
                .map(DocumentTag::getTagId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<UUID, Tag> tagsById = tagRepository.findAllById(tagIds).stream()
                .collect(Collectors.toMap(Tag::getId, Function.identity()));

        Map<UUID, List<String>> result = new HashMap<>();
        for (DocumentTag dt : documentTags) {
            UUID docId = dt.getDocumentId();
            UUID tagId = dt.getTagId();
            Tag tag = tagsById.get(tagId);
            if (docId != null && tag != null) {
                result.computeIfAbsent(docId, k -> new ArrayList<>()).add(tag.getName());
            }
        }
        return result;
    }

    private Set<UUID> loadBookmarkedDocumentIds(List<Document> documents, UUID currentUserId) {
        if (currentUserId == null || documents.isEmpty()) {
            return Collections.emptySet();
        }
        User user = userRepository.findById(currentUserId).orElse(null);
        if (user == null) {
            return Collections.emptySet();
        }
        List<DocumentBookmark> bookmarks = documentBookmarkRepository.findByUserAndActiveTrue(user);
        return bookmarks.stream()
                .map(DocumentBookmark::getDocument)
                .filter(Objects::nonNull)
                .map(Document::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void enrichCardDto(DocumentCardResponseDto dto,
                               Map<UUID, String> authorNames,
                               Map<UUID, List<String>> tagsByDocument,
                               Set<UUID> bookmarkedDocumentIds) {
        if (dto == null || dto.getId() == null) {
            return;
        }
        UUID documentId = UUID.fromString(dto.getId());
        dto.setAuthorName(authorNames.get(documentId));
        dto.setTags(tagsByDocument.getOrDefault(documentId, Collections.emptyList()));
        dto.setIsBookmarked(bookmarkedDocumentIds.contains(documentId));
    }
}

