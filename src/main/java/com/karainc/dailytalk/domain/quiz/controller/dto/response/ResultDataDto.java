package com.karainc.dailytalk.domain.quiz.controller.dto.response;


import com.karainc.dailytalk.domain.quiz.controller.dto.data.MemberDataDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultDataDto {
    private String status;
    private String message;
    private List<MemberDataDto> data;
}
