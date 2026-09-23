package com.karainc.dailytalk.domain.member.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakakoMemberDto {
    private String accessToken;
    private String name;
    private String nickname;
    private String email;
    private String memberType;
    private String phoneNumber;
    private String guardian;
    private String morePn;
}
