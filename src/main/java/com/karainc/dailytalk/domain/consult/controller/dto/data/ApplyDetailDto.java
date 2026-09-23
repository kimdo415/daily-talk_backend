package com.karainc.dailytalk.domain.consult.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyDetailDto {
    public Long consultIdx;
    private String rehabName;
    private Boolean status;
    private String detail;
}
