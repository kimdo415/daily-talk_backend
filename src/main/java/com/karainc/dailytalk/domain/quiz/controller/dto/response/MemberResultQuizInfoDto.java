package com.karainc.dailytalk.domain.quiz.controller.dto.response;

import com.karainc.dailytalk.domain.quiz.controller.dto.data.MemberQuizInfoDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResultQuizInfoDto {
    private String status;
    private String message;
    private MemberQuizInfoDto data;
}
