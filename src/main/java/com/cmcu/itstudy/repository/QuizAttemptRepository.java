package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.QuizAttempt;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {

    List<QuizAttempt> findByUserIdAndQuizIdOrderByAttemptNumberDesc(UUID userId, UUID quizId);

    Optional<QuizAttempt> findTopByUserIdAndQuizIdAndStatusOrderByStartTimeDesc(UUID userId, UUID quizId, String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select qa
            from QuizAttempt qa
            where qa.userId = :userId
              and qa.quiz.id = :quizId
              and upper(qa.status) = 'IN_PROGRESS'
              and qa.endTime is null
            order by qa.startTime desc
            """)
    List<QuizAttempt> findInProgressAttemptsForUpdate(@Param("userId") UUID userId, @Param("quizId") UUID quizId);

    long countByUserIdAndQuizIdAndStartTimeBetween(UUID userId, UUID quizId, LocalDateTime startTime, LocalDateTime endTime);

    List<QuizAttempt> findByUserIdOrderByStartTimeDesc(UUID userId);

    Page<QuizAttempt> findByUserId(UUID userId, Pageable pageable);

    @Query("""
            select distinct qa
            from QuizAttempt qa
            left join fetch qa.answers aa
            left join fetch aa.question qq
            left join fetch aa.selectedOption so
            where qa.id = :id
            """)
    Optional<QuizAttempt> findByIdWithAnswers(@Param("id") UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select qa from QuizAttempt qa where qa.id = :id")
    Optional<QuizAttempt> findByIdForUpdate(@Param("id") UUID id);
}
