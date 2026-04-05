package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.DocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentFileRepository extends JpaRepository<DocumentFile, UUID> {

    Optional<DocumentFile> findFirstByDocument_IdAndPrimaryTrue(UUID documentId);

    default Optional<DocumentFile> findByDocumentIdAndPrimaryTrue(UUID documentId) {
        return findFirstByDocument_IdAndPrimaryTrue(documentId);
    }
}
