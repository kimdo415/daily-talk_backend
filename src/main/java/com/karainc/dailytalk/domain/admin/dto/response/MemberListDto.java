package com.karainc.dailytalk.domain.admin.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberListDto {
    private Long memberIdx;
    private String profile;
    private String name;
    private String memberType;
    private String phoneNumber;
    private String morePn;
    private String day;
    private Boolean status;
}
