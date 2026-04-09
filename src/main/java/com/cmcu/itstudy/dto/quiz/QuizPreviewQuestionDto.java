package com.cmcu.itstudy.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizPreviewQuestionDto {

    private String questionId;
    private String questionText;
    private Integer sortOrder;
    private List<QuizPreviewOptionDto> options;
}
