package com.karainc.dailytalk.domain.quiz.controller.dto.response;


import com.karainc.dailytalk.domain.quiz.controller.dto.data.QuizDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizListDto {
    private String status;
    private String message;
    private List<QuizDto> data;
}
