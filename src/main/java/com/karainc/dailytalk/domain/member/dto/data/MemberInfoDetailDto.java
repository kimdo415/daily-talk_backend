package com.karainc.dailytalk.domain.member.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoDetailDto {
    private Long memberIdx;
    private String sex;
    private String name;
    private String nickName;
    private String memberId;
    private String memberType;
    private String phoneNumber;
    private String guardian;
    private String morePn;
    private String profile;
    private String email;
    private String address;
    private String birth;

    private String phone1;
    private String phone2;
    private String parentsSex;
    private String parentsBirth;
    private String memberShip;
    private Boolean mkService;
}
