package com.karainc.dailytalk.domain.contents.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultInfo {
    private String type;
    private String name;
    private String title;
    private Integer useCount;
}
