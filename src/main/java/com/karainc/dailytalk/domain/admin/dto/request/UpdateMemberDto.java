package com.karainc.dailytalk.domain.admin.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberDto {
    private long memberIdx;
    private String sex;
    private String intro;
    private String name;
    private String morePn;
    private String guardian;
    private String address;
    private String birth;
}
