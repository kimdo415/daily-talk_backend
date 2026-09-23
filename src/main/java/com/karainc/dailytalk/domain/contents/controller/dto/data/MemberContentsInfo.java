package com.karainc.dailytalk.domain.contents.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberContentsInfo {
    private long contentsIdx;
    private String contentsName;
    private String intro;
    private Integer price;
    private String contentsImage;
    private Integer useCount;
    private String duringDate;
}
