package com.karainc.dailytalk.domain.pay.controller.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundDto {
    private Long payIdx;
    private Integer amount;
}
