package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.document.DocumentDetailCommentsDto;

import java.util.UUID;

public interface CommentService {

    DocumentDetailCommentsDto loadCommentsForDocument(UUID documentId);
}
