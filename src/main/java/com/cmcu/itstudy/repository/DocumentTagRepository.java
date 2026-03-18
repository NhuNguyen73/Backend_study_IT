package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.DocumentTag;
import com.cmcu.itstudy.entity.DocumentTag.DocumentTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface DocumentTagRepository extends JpaRepository<DocumentTag, DocumentTagId> {

    List<DocumentTag> findByDocumentIdIn(Collection<UUID> documentIds);
}
