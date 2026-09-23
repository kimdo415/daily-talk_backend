package com.karainc.dailytalk.domain.member.dto.response;


import com.karainc.dailytalk.domain.member.dto.data.MemberInfoDetailDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoDto {
    private String status;
    private String message;
    private MemberInfoDetailDto data;
}
