package com.karainc.dailytalk.domain.language.contoller.dto.response;


import com.karainc.dailytalk.domain.language.contoller.dto.data.MemberData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllMemberDto {
    private String status;
    private String message;
    private List<MemberData> data;
}
