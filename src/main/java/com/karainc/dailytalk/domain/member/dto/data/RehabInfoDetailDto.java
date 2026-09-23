package com.karainc.dailytalk.domain.member.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RehabInfoDetailDto {
    private Long memberIdx;
    private String sex;
    private String memberId;
    private String name;
    private String nickName;
    private String email;
    private String memberType;
    private String phoneNumber;
    private String birth;
    private String address;

    private String guardian;
    private String phone1;
    private String phone2;
    private String parentsSex;
    private String parentsBirth;
    private Boolean mkService;
    private String detailAddress;

    private String activityArea;
    private List<String> cert;
    private List<String> careerDay;
    private List<String> career;
    private String division;
    private String intro;
    private String profile;
    private String checkEnum;
}
