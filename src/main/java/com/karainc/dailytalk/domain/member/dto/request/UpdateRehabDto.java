package com.karainc.dailytalk.domain.member.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UpdateRehabDto {
    private String sex;
    private String intro;
    private String name;
    private String birth;
    private String address;

    private String guardian;
    private String phone1;
    private String phone2;
    private String parentsSex;
    private String parentsBirth;
    private String detailAddress;
    private Boolean mkService;

    private String activityArea;
    private List<String> cert;
    private List<String> careerDay;
    private List<String> career;
    private String division;
}
