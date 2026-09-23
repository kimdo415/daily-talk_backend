package com.karainc.dailytalk.domain.consult.controller.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MsgDto {
    private String status;
    private String message;
    private String data;
}
