package com.karainc.dailytalk.domain.admin.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResultDto {
    private String status;
    private String message;
    private String data;
}
