package com.karainc.dailytalk.domain.dashboard.controller.dto.requset;


import com.karainc.dailytalk.domain.dashboard.controller.dto.data.pay.TotalPay;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashPayDto {
    private String status;
    private String message;
    private TotalPay data;
}
