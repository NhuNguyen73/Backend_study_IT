package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.QuizAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuizAttemptAnswerRepository extends JpaRepository<QuizAttemptAnswer, UUID> {

    List<QuizAttemptAnswer> findByAttemptId(UUID attemptId);
}
