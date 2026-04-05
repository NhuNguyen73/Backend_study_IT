package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, UUID> {

    @Query("""
            select qq.quiz.id, count(qq.id)
            from QuizQuestion qq
            where qq.quiz.id in :quizIds
            group by qq.quiz.id
            """)
    List<Object[]> countQuestionsGroupedByQuizId(@Param("quizIds") Collection<UUID> quizIds);
}
