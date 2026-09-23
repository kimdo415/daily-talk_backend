package com.karainc.dailytalk.domain.utils.smtp.controller.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto {
    private String status;
    private String message;
    private String data;
}
