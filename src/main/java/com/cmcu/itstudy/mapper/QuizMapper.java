package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.quiz.QuizHistoryItemDto;
import com.cmcu.itstudy.dto.quiz.QuizHistoryPageResponseDto;
import com.cmcu.itstudy.dto.quiz.QuizResultOptionDto;
import com.cmcu.itstudy.dto.quiz.QuizResultQuestionDto;
import com.cmcu.itstudy.dto.quiz.QuizResultResponseDto;
import com.cmcu.itstudy.entity.QuizAttempt;
import com.cmcu.itstudy.entity.QuizAttemptAnswer;
import com.cmcu.itstudy.entity.QuizQuestion;
import com.cmcu.itstudy.entity.QuizQuestionOption;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class QuizMapper {

    private QuizMapper() {
    }

    public static QuizResultResponseDto toQuizResultResponseDto(QuizAttempt attempt) {
        if (attempt == null) {
            return null;
        }

        return QuizResultResponseDto.builder()
                .score(attempt.getScore())
                .maxScore(attempt.getMaxScore())
                .scorePercent(attempt.getScorePercent())
                .status(attempt.getStatus())
                .totalQuestions(attempt.getTotalQuestions())
                .correctCount(attempt.getCorrectCount())
                .wrongCount(attempt.getWrongCount())
                .skippedCount(attempt.getSkippedCount())
                .startTime(attempt.getStartTime())
                .endTime(attempt.getEndTime())
                .questions(toResultQuestionDtos(attempt.getAnswers()))
                .build();
    }

    public static QuizHistoryItemDto toQuizHistoryItemDto(QuizAttempt attempt) {
        if (attempt == null) {
            return null;
        }
        return QuizHistoryItemDto.builder()
                .attemptId(uuidToString(attempt.getId()))
                .quizId(attempt.getQuiz() != null ? uuidToString(attempt.getQuiz().getId()) : null)
                .quizTitle(attempt.getQuiz() != null ? attempt.getQuiz().getTitle() : null)
                .score(attempt.getScore())
                .scorePercent(attempt.getScorePercent())
                .attemptDate(attempt.getStartTime())
                .status(attempt.getStatus())
                .build();
    }

    public static QuizHistoryPageResponseDto toQuizHistoryPageResponseDto(Page<QuizAttempt> attemptPage) {
        if (attemptPage == null) {
            return null;
        }
        List<QuizHistoryItemDto> items = attemptPage.getContent().stream()
                .map(QuizMapper::toQuizHistoryItemDto)
                .toList();
        return QuizHistoryPageResponseDto.builder()
                .items(items)
                .page(attemptPage.getNumber())
                .totalPages(attemptPage.getTotalPages())
                .totalItems(attemptPage.getTotalElements())
                .build();
    }

    private static List<QuizResultQuestionDto> toResultQuestionDtos(List<QuizAttemptAnswer> attemptAnswers) {
        if (attemptAnswers == null || attemptAnswers.isEmpty()) {
            return Collections.emptyList();
        }

        List<QuizAttemptAnswer> sortedAnswers = new ArrayList<>(attemptAnswers);
        sortedAnswers.sort(Comparator.comparing(
                a -> a != null && a.getQuestion() != null ? a.getQuestion().getSortOrder() : null,
                Comparator.nullsLast(Integer::compareTo)
        ));

        Map<UUID, QuizAttemptAnswer> answersByQuestionId = new LinkedHashMap<>();
        for (QuizAttemptAnswer answer : sortedAnswers) {
            if (answer == null || answer.getQuestion() == null || answer.getQuestion().getId() == null) {
                continue;
            }
            answersByQuestionId.putIfAbsent(answer.getQuestion().getId(), answer);
        }

        List<QuizResultQuestionDto> questions = new ArrayList<>();
        for (QuizAttemptAnswer answer : answersByQuestionId.values()) {
            QuizQuestion question = answer.getQuestion();
            List<QuizResultOptionDto> options = toResultOptionDtos(question.getOptions(), answer.getSelectedOption());
            questions.add(QuizResultQuestionDto.builder()
                    .questionId(uuidToString(question.getId()))
                    .content(question.getQuestionText())
                    .answers(options)
                    .explanation(question.getExplanation())
                    .isCorrect(answer.getIsCorrect())
                    .build());
        }

        return questions;
    }

    private static List<QuizResultOptionDto> toResultOptionDtos(List<QuizQuestionOption> options, QuizQuestionOption selectedOption) {
        if (options == null || options.isEmpty()) {
            return Collections.emptyList();
        }

        List<QuizQuestionOption> sortedOptions = new ArrayList<>(options);
        sortedOptions.sort(Comparator.comparing(
                QuizQuestionOption::getSortOrder,
                Comparator.nullsLast(Integer::compareTo)
        ));

        UUID selectedOptionId = selectedOption != null ? selectedOption.getId() : null;
        List<QuizResultOptionDto> result = new ArrayList<>(sortedOptions.size());
        for (QuizQuestionOption option : sortedOptions) {
            if (option == null) {
                continue;
            }
            UUID optionId = option.getId();
            result.add(QuizResultOptionDto.builder()
                    .answerId(uuidToString(optionId))
                    .content(option.getContent())
                    .isCorrect(option.getIsCorrect())
                    .isSelected(Objects.equals(optionId, selectedOptionId))
                    .build());
        }
        return result;
    }

    private static String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }
}
