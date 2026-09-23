package com.karainc.dailytalk.domain.quiz.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private long categoryIdx;
    private long quizIdx;
    private String quizTitle;
    private String quizSubTitle;
    private String quizAge;
    private String quizLevel;
    private String titleImage;
    private int qCount;
    private Boolean view;
}
