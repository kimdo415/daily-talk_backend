package com.karainc.dailytalk.domain.quiz.controller.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizMakeResultDto {
    private String status;
    private String message;
    private Long data;
}
