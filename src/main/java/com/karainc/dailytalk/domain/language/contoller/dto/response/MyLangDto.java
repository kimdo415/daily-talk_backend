package com.karainc.dailytalk.domain.language.contoller.dto.response;


import com.karainc.dailytalk.domain.language.contoller.dto.data.LangData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyLangDto {
    private String status;
    private String message;
    private List<LangData> data;
    private String nextDate;
}
