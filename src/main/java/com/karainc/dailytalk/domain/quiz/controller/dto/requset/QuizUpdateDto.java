package com.karainc.dailytalk.domain.quiz.controller.dto.requset;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class QuizUpdateDto {
    private Long quizIdx;
    private int quizNumber;
    private String question;
    private List<String> choices;
    private String answer;
}
