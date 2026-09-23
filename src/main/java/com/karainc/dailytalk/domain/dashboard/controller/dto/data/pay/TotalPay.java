package com.karainc.dailytalk.domain.dashboard.controller.dto.data.pay;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalPay {
    private OneDay day;
    private OneWeek week;
    private OneMonth month;
}
