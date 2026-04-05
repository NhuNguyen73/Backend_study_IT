package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.DocumentView;
import com.cmcu.itstudy.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DocumentViewRepository extends JpaRepository<DocumentView, Long> {

    long countByDocument(Document document);

    long countByDocument_Id(UUID documentId);

    default long countByDocumentId(UUID documentId) {
        return countByDocument_Id(documentId);
    }

    long countByDocumentAndViewedAtAfter(Document document, LocalDateTime from);

    @Query("""
            select count(v)
            from DocumentView v
            where v.document = :document
              and v.viewedAt >= :from
            """)
    long countRecentViews(@Param("document") Document document, @Param("from") LocalDateTime from);
}
