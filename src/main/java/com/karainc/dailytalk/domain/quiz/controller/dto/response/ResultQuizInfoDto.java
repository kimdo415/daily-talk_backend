package com.karainc.dailytalk.domain.quiz.controller.dto.response;


import com.karainc.dailytalk.domain.quiz.controller.dto.data.QuizInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultQuizInfoDto {
    private String status;
    private String message;
    private QuizInfoDto data;
}
