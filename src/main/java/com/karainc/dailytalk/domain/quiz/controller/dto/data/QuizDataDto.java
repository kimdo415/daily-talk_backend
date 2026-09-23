package com.karainc.dailytalk.domain.quiz.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizDataDto {
    private Long quizDataIdx;
    private String profile;
    private Long memberIdx;
    private String name;
    private String quizTitle;
    private int point;
    private String date;
}
