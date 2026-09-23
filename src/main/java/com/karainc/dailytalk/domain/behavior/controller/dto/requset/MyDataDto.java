package com.karainc.dailytalk.domain.behavior.controller.dto.requset;


import com.karainc.dailytalk.domain.behavior.controller.dto.data.MyData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyDataDto {
    private String status;
    private String message;
    private MyData data;
}
