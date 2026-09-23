package com.karainc.dailytalk.domain.pay.controller.dto.response;


import com.karainc.dailytalk.domain.pay.controller.dto.data.MyPayDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPayDetailDto {
    private String status;
    private String message;
    private MyPayDetail data;
}
