package com.karainc.dailytalk.domain.utils.coolsms.controller.dto.Response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultSmsDto {
    private String status;
    private String message;
    private String data;
}
