package com.karainc.dailytalk.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberDto {
    private String sex;
    private String intro;
    private String name;
    private String morePn;
    private String guardian;
    private String address;
    private String birth;

    private String phone1;
    private String phone2;
    private String parentsSex;
    private String parentsBirth;
    private String memberShip;
    private Boolean mkService;
}
