package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.Document;
import com.cmcu.itstudy.enums.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID>, JpaSpecificationExecutor<Document> {

    Page<Document> findByStatusAndDeletedFalseOrderByCreatedAtDesc(DocumentStatus status, Pageable pageable);

    Page<Document> findByStatusOrderByCreatedAtDesc(DocumentStatus status, Pageable pageable);

    Page<Document> findByStatusAndDeletedFalseOrderByViewCountDescDownloadCountDescCreatedAtDesc(DocumentStatus status, Pageable pageable);

    Page<Document> findByStatusOrderByViewCountDescDownloadCountDescCreatedAtDesc(DocumentStatus status, Pageable pageable);

    long countByStatusAndDeletedFalse(DocumentStatus status);

    long countByStatusAndDeletedFalseAndCreatedByIsNotNull(DocumentStatus status);

    @Query("""
            select coalesce(sum(d.downloadCount), 0)
            from Document d
            where d.status = :status
              and d.deleted = false
            """)
    long sumDownloadCountByStatusAndDeletedFalse(@Param("status") DocumentStatus status);

    @Query("""
            select d
            from Document d
            where d.status = :status
              and d.deleted = false
              and d.id in (
                  select dt.document.id
                  from DocumentTag dt
                  where dt.tagId in :tagIds
                  group by dt.document.id
                  having count(distinct dt.tagId) = :tagCount
              )
            """)
    Page<Document> findByStatusAndDeletedFalseAndAllTags(@Param("status") DocumentStatus status,
                                                         @Param("tagIds") List<UUID> tagIds,
                                                         @Param("tagCount") long tagCount,
                                                         Pageable pageable);

    @Query("""
            select d
            from Document d
            where d.status = :status
              and d.deleted = false
              and lower(d.title) like lower(concat('%', :keyword, '%'))
            """)
    Page<Document> searchByStatusAndDeletedFalseAndTitleContaining(@Param("status") DocumentStatus status,
                                                                   @Param("keyword") String keyword,
                                                                   Pageable pageable);

    @Query("""
            select d
            from Document d
            where d.status = :status
              and d.deleted = false
              and lower(d.title) like lower(concat('%', :keyword, '%'))
            order by d.createdAt desc
            """)
    Page<Document> searchByStatusAndDeletedFalseAndTitleContainingOrderByCreatedAtDesc(@Param("status") DocumentStatus status,
                                                                                       @Param("keyword") String keyword,
                                                                                       Pageable pageable);

    @Query("""
            select d
            from Document d
            where d.status = :status
              and d.deleted = false
              and d.createdAt >= :from
            order by d.viewCount desc, d.downloadCount desc, d.createdAt desc
            """)
    List<Document> findTrendingDocuments(@Param("status") DocumentStatus status,
                                         @Param("from") LocalDateTime from,
                                         Pageable pageable);

    @Query("""
            select d
            from DocumentView v
            join v.document d
            where d.status = :status
              and d.deleted = false
              and v.viewedAt >= :from
            group by d
            order by count(v) desc, d.createdAt desc
            """)
    List<Document> findTrendingByViews(@Param("status") DocumentStatus status,
                                       @Param("from") LocalDateTime from,
                                       Pageable pageable);

    @Query("""
            select d
            from DocumentDownload v
            join v.document d
            where d.status = :status
              and d.deleted = false
              and v.downloadedAt >= :from
            group by d
            order by count(v) desc, d.createdAt desc
            """)
    List<Document> findTrendingByDownloads(@Param("status") DocumentStatus status,
                                           @Param("from") LocalDateTime from,
                                           Pageable pageable);

    @Query("""
            select d
            from Document d
            where d.status = :status
              and d.deleted = false
              and d.category.id = :categoryId
              and d.id <> :excludeDocumentId
            order by d.viewCount desc, d.downloadCount desc, d.createdAt desc
            """)
    Slice<Document> findRelatedDocumentsForDetail(@Param("status") DocumentStatus status,
                                                  @Param("categoryId") UUID categoryId,
                                                  @Param("excludeDocumentId") UUID excludeDocumentId,
                                                  Pageable pageable);

    @EntityGraph(attributePaths = {"category", "createdBy", "documentTags.tag"})
    List<Document> findByCreatedByAndDeletedFalseOrderByCreatedAtDesc(com.cmcu.itstudy.entity.User createdBy);
}
