package com.cmcu.itstudy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "tbl_quiz_attempts")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "uniqueidentifier")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Quiz quiz;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "score")
    private Double score;

    @Column(name = "max_score")
    private Double maxScore;

    @Column(name = "score_percent")
    private Double scorePercent;

    @Column(name = "status", nullable = false, columnDefinition = "NVARCHAR(20)")
    private String status;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "correct_count")
    private Integer correctCount;

    @Column(name = "wrong_count")
    private Integer wrongCount;

    @Column(name = "skipped_count")
    private Integer skippedCount;

    @OneToMany(mappedBy = "attempt", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<QuizAttemptAnswer> answers = new ArrayList<>();
}
