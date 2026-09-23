package com.karainc.dailytalk.domain.consult.controller.dto.response;


import com.karainc.dailytalk.domain.consult.controller.dto.data.GetApplyDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminAppDto {
    private String status;
    private String message;
    private List<GetApplyDto> data;
}
