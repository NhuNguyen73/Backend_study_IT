package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.entity.Document;

import java.util.List;
import java.util.UUID;

public interface DocumentService {

    Document getById(UUID id);

    List<Document> getRelatedDocuments(UUID documentId, int limit);
}
