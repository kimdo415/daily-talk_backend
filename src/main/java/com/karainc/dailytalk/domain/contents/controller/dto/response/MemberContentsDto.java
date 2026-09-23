package com.karainc.dailytalk.domain.contents.controller.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberContentsDto {
    private String subStart;
    private String subEnd;
    private String subConName;
    private String subConTitle;

    private Integer checkUseCount;
    private String checkName;
    private String checkTitle;

    private Integer consultUseCount;
    private String consultName;
    private String consultTitle;
}

