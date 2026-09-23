package com.karainc.dailytalk.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RehabMemberDto {
    private String memberId;
    private String password;
    private String memberType;
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber;

    // 재활자 정보
    private String activityArea;
    private List<String> cert;
    private List<String> careerDay;
    private List<String> career;
    private String division;
    private String intro;
}
