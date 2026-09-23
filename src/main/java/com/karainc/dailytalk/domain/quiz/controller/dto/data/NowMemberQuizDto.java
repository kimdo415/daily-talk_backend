package com.karainc.dailytalk.domain.quiz.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NowMemberQuizDto {
    private String memberId;
    private String name;
    private String profile;
    private Integer point;
    private String correctRate;
    private String incorrectRate;
    private Integer testTime;
    private String testDay;
}
