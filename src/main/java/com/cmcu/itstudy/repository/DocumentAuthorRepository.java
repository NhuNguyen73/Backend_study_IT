package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.DocumentAuthor;
import com.cmcu.itstudy.entity.DocumentAuthor.DocumentAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface DocumentAuthorRepository extends JpaRepository<DocumentAuthor, DocumentAuthorId> {

    List<DocumentAuthor> findByDocumentIdIn(Collection<UUID> documentIds);
}
