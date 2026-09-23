package com.karainc.dailytalk.domain.quiz.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizDto {
    private Long quizIdx;
    private Long categoryIdx;
    private String category;
    private int qCount;
    private String quizTitle;
    private String titleImage;
    private String quizSubTitle;
    private String quizAge;
    private String quizLevel;
    private Boolean view;
}
