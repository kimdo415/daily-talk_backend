package com.karainc.dailytalk.domain.pay.controller.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayDto {
    private String customerKey;
    private String authKey;
}
