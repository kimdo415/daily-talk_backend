package com.karainc.dailytalk.domain.member.dto.response;


import com.karainc.dailytalk.domain.member.dto.data.RehabInfoDetailDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RehabInfoDto {
    private String status;
    private String message;
    private RehabInfoDetailDto data;
}
