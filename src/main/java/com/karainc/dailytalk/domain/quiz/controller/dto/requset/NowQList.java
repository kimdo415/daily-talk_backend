package com.karainc.dailytalk.domain.quiz.controller.dto.requset;


import com.karainc.dailytalk.domain.quiz.controller.dto.data.NowMemberQuizDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NowQList {
    private String status;
    private String message;
    private List<NowMemberQuizDto> data;
}
