package com.karainc.dailytalk.domain.dashboard.controller.dto.requset;


import com.karainc.dailytalk.domain.dashboard.controller.dto.data.DashBoardDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashDto {
    private String status;
    private String message;
    private DashBoardDto data;
}
