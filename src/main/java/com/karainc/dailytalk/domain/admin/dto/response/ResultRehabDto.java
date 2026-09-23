package com.karainc.dailytalk.domain.admin.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultRehabDto {
    private String status;
    private String message;
    private List<RehabListDto> data;
}
