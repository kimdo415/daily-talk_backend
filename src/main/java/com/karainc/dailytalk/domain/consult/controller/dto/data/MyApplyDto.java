package com.karainc.dailytalk.domain.consult.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyApplyDto {
    private long consultIdx;
    private String rehabName;
    private String startDate;
    private String endDate;
    private String detail;
    private boolean status;
}
