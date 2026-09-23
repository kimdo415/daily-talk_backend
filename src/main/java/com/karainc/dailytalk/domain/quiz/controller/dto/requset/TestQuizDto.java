package com.karainc.dailytalk.domain.quiz.controller.dto.requset;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestQuizDto {
    private Long quizIdx;
    private List<String> answers;
    private int testTime;
}
