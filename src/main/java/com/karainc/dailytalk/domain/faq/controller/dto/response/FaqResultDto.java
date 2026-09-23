package com.karainc.dailytalk.domain.faq.controller.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqResultDto {
    private String status;
    private String message;
    private String data;
}
