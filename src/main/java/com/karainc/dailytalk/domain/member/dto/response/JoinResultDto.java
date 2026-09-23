package com.karainc.dailytalk.domain.member.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinResultDto {
    private String status;
    private String message;
    private String data;
}
