package com.karainc.dailytalk.domain.quiz.controller.dto.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MemberQuizInfoDto {
    private Long quizId;
    private Long categoryIdx;
    private String category;
    private String quizTitle;
    private String quizSubTitle;
    private String quizAge;
    private String quizLevel;
    private String titleImage;
    private List<MemberQuizInfoDetailDto> qna;
}
