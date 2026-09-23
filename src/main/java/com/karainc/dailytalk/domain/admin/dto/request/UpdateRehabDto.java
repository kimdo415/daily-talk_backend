package com.karainc.dailytalk.domain.admin.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateRehabDto {
    private long memberIdx;
    private String sex;
    private String intro;
    private String name;
    private String address;
    private String birth;

    private String activityArea;
    private List<String> cert;
    private List<String> careerDay;
    private List<String> career;
    private String division;
}
