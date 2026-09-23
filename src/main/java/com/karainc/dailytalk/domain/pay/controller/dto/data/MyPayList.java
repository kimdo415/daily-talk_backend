package com.karainc.dailytalk.domain.pay.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPayList {
    private Long payIdx; // 결제 pk
    private String contentsName; // 컨텐츠 이름
    private String orderCode; // 주문번호
    private String orderDate; // 주문 날짜
    private String howTo; // 결제 방법
    private Boolean Status; // 환불 여부
}
