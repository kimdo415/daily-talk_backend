package com.karainc.dailytalk.domain.site.controller.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteMsgDto {
    private String status;
    private String message;
    private String data;
}
