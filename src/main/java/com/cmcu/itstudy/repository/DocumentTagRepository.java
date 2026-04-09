package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.DocumentTag;
import com.cmcu.itstudy.entity.DocumentTag.DocumentTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface DocumentTagRepository extends JpaRepository<DocumentTag, DocumentTagId> {

    List<DocumentTag> findByDocumentIdIn(Collection<UUID> documentIds);

    List<DocumentTag> findByDocumentId(UUID documentId);

    @Modifying
    @Query("DELETE FROM DocumentTag dt WHERE dt.documentId = :documentId")
    void deleteAllByDocumentId(@Param("documentId") UUID documentId);
}
