package com.cmcu.itstudy.controller;

import com.cmcu.itstudy.dto.common.ApiResponse;
import com.cmcu.itstudy.dto.quiz.QuizHistoryPageResponseDto;
import com.cmcu.itstudy.dto.quiz.QuizPreviewResponseDto;
import com.cmcu.itstudy.dto.quiz.QuizResultResponseDto;
import com.cmcu.itstudy.dto.quiz.StartQuizResponseDto;
import com.cmcu.itstudy.dto.quiz.SubmitQuizRequestDto;
import com.cmcu.itstudy.service.contract.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/{quizId}/start")
    public ResponseEntity<ApiResponse<StartQuizResponseDto>> startQuiz(@PathVariable("quizId") UUID quizId) {
        StartQuizResponseDto data = quizService.startQuiz(quizId);
        return ResponseEntity.ok(ApiResponse.success(data, "Quiz started"));
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<QuizResultResponseDto>> submitQuiz(@Valid @RequestBody SubmitQuizRequestDto request) {
        QuizResultResponseDto data = quizService.submitQuiz(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Quiz submitted"));
    }

    @GetMapping("/attempts/{attemptId}")
    public ResponseEntity<ApiResponse<QuizResultResponseDto>> getQuizResult(@PathVariable("attemptId") UUID attemptId) {
        QuizResultResponseDto data = quizService.getQuizResult(attemptId);
        return ResponseEntity.ok(ApiResponse.success(data, "Quiz result"));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<QuizHistoryPageResponseDto>> getQuizHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        QuizHistoryPageResponseDto data = quizService.getQuizHistory(page, size);
        return ResponseEntity.ok(ApiResponse.success(data, "Quiz history"));
    }

    @GetMapping("/{quizId}/preview")
    public ResponseEntity<ApiResponse<QuizPreviewResponseDto>> getQuizPreview(@PathVariable("quizId") UUID quizId) {
        QuizPreviewResponseDto data = quizService.getQuizPreview(quizId);
        return ResponseEntity.ok(ApiResponse.success(data, "Quiz preview"));
    }
}
