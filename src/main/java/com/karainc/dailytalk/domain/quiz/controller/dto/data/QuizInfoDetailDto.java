package com.karainc.dailytalk.domain.quiz.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizInfoDetailDto {
    private String question;
    private List<String> choices;
    private String image;
    private String answer;
}
