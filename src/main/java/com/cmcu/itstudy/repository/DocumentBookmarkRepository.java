package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.DocumentBookmark;
import com.cmcu.itstudy.entity.Document;
import com.cmcu.itstudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentBookmarkRepository extends JpaRepository<DocumentBookmark, UUID> {

    boolean existsByUserAndDocumentAndActiveTrue(User user, Document document);

    List<DocumentBookmark> findByUserAndActiveTrue(User user);

    void deleteByUserAndDocument(User user, Document document);

    long countByDocumentAndActiveTrue(Document document);
}
