package com.karainc.dailytalk.domain.consult.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RehabList {
    private long memberIdx;
    private String rehabProfile;
    private String name;
    private String division;
    private String activityArea;
    private String intro;
    private List<String> cert;
    private List<String> careerDay;
    private List<String> career;
 }
