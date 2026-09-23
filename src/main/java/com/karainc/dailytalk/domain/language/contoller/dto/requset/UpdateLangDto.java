package com.karainc.dailytalk.domain.language.contoller.dto.requset;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLangDto {
    private Integer index;
    private String languages;
    private Integer duringDate;
    private String limitDate;
    private String languageEnum;
}
