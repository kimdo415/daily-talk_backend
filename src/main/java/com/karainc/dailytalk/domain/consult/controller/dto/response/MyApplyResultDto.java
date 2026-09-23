package com.karainc.dailytalk.domain.consult.controller.dto.response;


import com.karainc.dailytalk.domain.consult.controller.dto.data.MyApplyDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyApplyResultDto {
    private String status;
    private String message;
    private List<MyApplyDto> data;
}
