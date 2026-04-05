package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.DocumentQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentQuizRepository extends JpaRepository<DocumentQuiz, UUID> {

    @Query("""
            select dq
            from DocumentQuiz dq
            join fetch dq.quiz q
            where dq.document.id = :documentId
            order by dq.sortOrder asc, dq.id asc
            """)
    List<DocumentQuiz> findAllByDocumentIdWithQuiz(@Param("documentId") UUID documentId);
}
