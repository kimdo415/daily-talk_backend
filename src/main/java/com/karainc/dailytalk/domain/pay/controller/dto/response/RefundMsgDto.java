package com.karainc.dailytalk.domain.pay.controller.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundMsgDto {
    private String status;
    private String message;
    private String data;
}
