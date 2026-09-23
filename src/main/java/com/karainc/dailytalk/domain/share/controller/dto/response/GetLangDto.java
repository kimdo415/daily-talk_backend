package com.karainc.dailytalk.domain.share.controller.dto.response;


import com.karainc.dailytalk.domain.share.controller.dto.data.GetShareLang;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetLangDto {
    private String status;
    private String message;
    private GetShareLang data;
}
