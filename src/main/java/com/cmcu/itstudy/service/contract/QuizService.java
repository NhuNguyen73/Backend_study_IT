package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.document.DocumentDetailQuizDto;

import java.util.List;
import java.util.UUID;

public interface QuizService {

    List<DocumentDetailQuizDto> loadQuizzesForDocument(UUID documentId);
}
