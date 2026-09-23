package com.karainc.dailytalk.domain.admin.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultMemberInfoDetailDto {
    private String status;
    private String message;
    private AMemberInfoDto data;
}
