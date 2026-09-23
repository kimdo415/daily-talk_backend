package com.karainc.dailytalk.domain.quiz.controller.dto.requset;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelDto {
    private long quizIdx;
    private String quizSubTitle;
    private String quizLevel;
    private String quizAge;
}
