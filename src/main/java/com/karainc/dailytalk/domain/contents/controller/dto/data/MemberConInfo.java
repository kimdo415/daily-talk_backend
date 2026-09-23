package com.karainc.dailytalk.domain.contents.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberConInfo {
    private Long contentsIdx;
    private String contentsName;
    private String intro;
    private Integer price;
    private String contentsImage;
    private String contentType;
    private String duringDate;
    private Integer useCount;
}
