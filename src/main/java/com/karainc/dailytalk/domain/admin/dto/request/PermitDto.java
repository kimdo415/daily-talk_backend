package com.karainc.dailytalk.domain.admin.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermitDto {
    private Long memberIdx;
    private String checkEnum;
    private String reason;
}
