package com.karainc.dailytalk.domain.animation.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyContentsInfo {
    private Long aniIdx;
    private String categoryName;
    private String aniName;
    private String date;
    private Integer playTime;
    private Integer replayCount;
    private Integer outCount;
    private String rate;
}
