package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.QuizQuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizQuestionOptionRepository extends JpaRepository<QuizQuestionOption, UUID> {
}
