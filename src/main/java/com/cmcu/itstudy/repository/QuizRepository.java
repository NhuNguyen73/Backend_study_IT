package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {
}
