package com.karainc.dailytalk.domain.member.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String memberId;
    private String password;
    private String name;
    private String nickname;
    private String email;
    private String memberType;
    private String phoneNumber;
    private String guardian;
    private String morePn;
}
