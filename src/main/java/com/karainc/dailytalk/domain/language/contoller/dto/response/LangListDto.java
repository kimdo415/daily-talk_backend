package com.karainc.dailytalk.domain.language.contoller.dto.response;


import com.karainc.dailytalk.domain.language.contoller.dto.data.LangInfo;
import com.karainc.dailytalk.domain.language.entity.Language;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LangListDto {
    private String status;
    private String message;
    private LangInfo data;
}
