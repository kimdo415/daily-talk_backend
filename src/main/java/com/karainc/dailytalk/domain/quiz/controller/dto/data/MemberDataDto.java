package com.karainc.dailytalk.domain.quiz.controller.dto.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDataDto {

    private Long categoryIdx;
    private String category;
    private Long quizIdx;
    private String quizTitle;
    private int point;
    private String correctRate;
    private String incorrectRate;
    private int testTime;
    private String testDay;
}
