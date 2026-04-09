package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.document.DocumentDetailQuizDto;
import com.cmcu.itstudy.dto.document.QuizListPageResponseDto;
import com.cmcu.itstudy.dto.quiz.QuizHistoryPageResponseDto;
import com.cmcu.itstudy.dto.quiz.QuizPreviewResponseDto;
import com.cmcu.itstudy.dto.quiz.QuizResultResponseDto;
import com.cmcu.itstudy.dto.quiz.StartQuizResponseDto;
import com.cmcu.itstudy.dto.quiz.SubmitQuizRequestDto;

import java.util.List;
import java.util.UUID;

public interface QuizService {

    List<DocumentDetailQuizDto> loadQuizzesForDocument(UUID documentId);

    QuizListPageResponseDto getQuizzesByDocument(UUID documentId, int page, int size);

    StartQuizResponseDto startQuiz(UUID quizId);

    QuizResultResponseDto submitQuiz(SubmitQuizRequestDto request);

    QuizResultResponseDto getQuizResult(UUID attemptId);

    QuizHistoryPageResponseDto getQuizHistory(int page, int size);

    QuizPreviewResponseDto getQuizPreview(UUID quizId);
}
