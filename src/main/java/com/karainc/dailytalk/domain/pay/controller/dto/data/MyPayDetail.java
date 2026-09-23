package com.karainc.dailytalk.domain.pay.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPayDetail {
    private Long payIdx;
    private String contentsImage;
    private String contentTitle;
    private String intro;
    private String amount;
}
