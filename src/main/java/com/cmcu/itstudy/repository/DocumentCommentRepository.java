package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.DocumentComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentCommentRepository extends JpaRepository<DocumentComment, UUID> {

    long countByDocument_IdAndDeletedFalse(UUID documentId);

    default long countByDocumentId(UUID documentId) {
        return countByDocument_IdAndDeletedFalse(documentId);
    }

    @Query("""
            select c
            from DocumentComment c
            join fetch c.author
            where c.document.id = :documentId
              and c.deleted = false
              and c.pinned = true
            order by c.createdAt desc
            """)
    List<DocumentComment> findPinnedWithAuthor(@Param("documentId") UUID documentId, Pageable pageable);

    @Query("""
            select c
            from DocumentComment c
            join fetch c.author
            where c.document.id = :documentId
              and c.deleted = false
              and c.parent is null
            order by c.createdAt desc
            """)
    List<DocumentComment> findTop5ByDocumentIdOrderByCreatedAtDesc(@Param("documentId") UUID documentId, Pageable pageable);
}
