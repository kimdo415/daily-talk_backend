package com.karainc.dailytalk.domain.language.contoller.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminLangDto {
    private String status;
    private String message;
    private List<String> data;
}
