package com.karainc.dailytalk.domain.member.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultDto {
    private String status;
    private String message;
    private String data;
}
