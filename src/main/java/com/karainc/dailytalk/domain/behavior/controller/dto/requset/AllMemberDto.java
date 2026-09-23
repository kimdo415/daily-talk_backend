package com.karainc.dailytalk.domain.behavior.controller.dto.requset;


import com.karainc.dailytalk.domain.behavior.controller.dto.data.AllData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllMemberDto {
    private String status;
    private String message;
    private List<AllData> data;
}
