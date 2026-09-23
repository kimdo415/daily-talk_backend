package com.karainc.dailytalk.domain.member.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoRehabMemberDto {
    private String accessToken;
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
