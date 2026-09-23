package com.karainc.dailytalk.domain.behavior.controller.dto.response;

import com.karainc.dailytalk.domain.behavior.controller.dto.data.BeData;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminBeDto {
    private String status;
    private String message;
    private BeData data;
}
