package com.karainc.dailytalk.domain.faq.controller.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqUpdateDto {
    private long faqId;
    private String faqTitle;
    private String faqContent;
}
