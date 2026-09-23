package com.karainc.dailytalk.domain.pay.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayList {
    private Long payIdx;
    private String memberId;
    private String memberName;
    private String payDate;
    private String amount;
    private String contentsName;
    private Boolean refundStatus;
    private String refundDate;
    private String refundAmount;
}
