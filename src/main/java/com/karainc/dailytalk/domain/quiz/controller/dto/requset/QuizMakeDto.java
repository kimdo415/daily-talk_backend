package com.karainc.dailytalk.domain.quiz.controller.dto.requset;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuizMakeDto {
    private Long quizIdx;
    private String question;
    private List<String> choices;
    private String answer;
}
