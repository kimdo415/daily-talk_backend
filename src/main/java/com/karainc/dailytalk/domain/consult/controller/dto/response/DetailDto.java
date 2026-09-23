package com.karainc.dailytalk.domain.consult.controller.dto.response;


import com.karainc.dailytalk.domain.consult.controller.dto.data.ApplyDetailDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailDto {
    private String status;
    private String message;
    private ApplyDetailDto data;
}
