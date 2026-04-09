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
public class QuizResultQuestionDto {

    private String questionId;
    private String content;
    private List<QuizResultOptionDto> answers;
    private String explanation;
    private Boolean isCorrect;
}
