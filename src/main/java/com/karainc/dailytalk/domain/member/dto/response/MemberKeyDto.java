package com.karainc.dailytalk.domain.member.dto.response;


import com.karainc.dailytalk.domain.admin.dto.data.MemberTypeDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberKeyDto {
    private String status;
    private String message;
    private MemberTypeDto data;
}
