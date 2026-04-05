package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.document.DocumentDetailQuizDto;
import com.cmcu.itstudy.entity.DocumentQuiz;
import com.cmcu.itstudy.entity.Quiz;
import com.cmcu.itstudy.mapper.DocumentMapper;
import com.cmcu.itstudy.repository.DocumentQuizRepository;
import com.cmcu.itstudy.repository.QuizQuestionRepository;
import com.cmcu.itstudy.service.contract.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    private final DocumentQuizRepository documentQuizRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    public QuizServiceImpl(DocumentQuizRepository documentQuizRepository,
                             QuizQuestionRepository quizQuestionRepository) {
        this.documentQuizRepository = documentQuizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentDetailQuizDto> loadQuizzesForDocument(UUID documentId) {
        List<DocumentQuiz> links = documentQuizRepository.findAllByDocumentIdWithQuiz(documentId);
        if (links.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> quizIds = links.stream()
                .map(DocumentQuiz::getQuiz)
                .filter(Objects::nonNull)
                .map(Quiz::getId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<Object[]> countRows = quizIds.isEmpty()
                ? Collections.emptyList()
                : quizQuestionRepository.countQuestionsGroupedByQuizId(quizIds);
        Map<UUID, Long> counts = DocumentMapper.toQuizQuestionCountMap(countRows);

        return links.stream()
                .map(DocumentQuiz::getQuiz)
                .filter(Objects::nonNull)
                .map(q -> DocumentMapper.toQuizDto(q, counts.getOrDefault(q.getId(), 0L)))
                .collect(Collectors.toList());
    }
}
