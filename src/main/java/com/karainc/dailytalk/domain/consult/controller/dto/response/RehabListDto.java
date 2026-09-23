package com.karainc.dailytalk.domain.consult.controller.dto.response;


import com.karainc.dailytalk.domain.consult.controller.dto.data.RehabList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RehabListDto {
    private String status;
    private String message;
    private List<RehabList> data;
}
