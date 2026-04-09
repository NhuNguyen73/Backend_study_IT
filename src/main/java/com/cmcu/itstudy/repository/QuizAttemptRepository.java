package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.QuizAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {

    List<QuizAttempt> findByUserIdAndQuizIdOrderByAttemptNumberDesc(UUID userId, UUID quizId);

    long countByUserIdAndQuizIdAndStartTimeBetween(UUID userId, UUID quizId, LocalDateTime startTime, LocalDateTime endTime);

    List<QuizAttempt> findByUserIdOrderByStartTimeDesc(UUID userId);

    Page<QuizAttempt> findByUserId(UUID userId, Pageable pageable);

    @Query("""
            select distinct qa
            from QuizAttempt qa
            join fetch qa.answers aa
            join fetch aa.question qq
            left join fetch qq.options qo
            left join fetch aa.selectedOption so
            where qa.id = :id
            """)
    Optional<QuizAttempt> findByIdWithAnswers(@Param("id") UUID id);
}
