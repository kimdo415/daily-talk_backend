package com.karainc.dailytalk.domain.faq.controller.dto.response;

import com.karainc.dailytalk.domain.faq.entity.Faq;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllFaqDto {
    private String status;
    private String message;
    private List<Faq> data;
}
