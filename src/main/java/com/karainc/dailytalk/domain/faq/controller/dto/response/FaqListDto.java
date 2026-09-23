package com.karainc.dailytalk.domain.faq.controller.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqListDto {
    private Long idx;
    private String title;
    private String content;
}
