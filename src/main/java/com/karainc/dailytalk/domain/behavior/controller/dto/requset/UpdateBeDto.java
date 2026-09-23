package com.karainc.dailytalk.domain.behavior.controller.dto.requset;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBeDto {
    private int index;
    private String behaviors;
    private String behaviorEnum;
    private Integer duringDate;
    private String limitDate;
}
