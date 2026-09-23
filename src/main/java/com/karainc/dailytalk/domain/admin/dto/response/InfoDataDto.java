package com.karainc.dailytalk.domain.admin.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoDataDto {
    private String status;
    private String message;
    private ReInfoDto data;
}
