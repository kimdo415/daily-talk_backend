package com.karainc.dailytalk.domain.animation.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CtList {
    private long categoryIdx;
    private long aniIdx;
    private String aniTitle;
    private String aniSubTitle;
    private String aniAge;
    private String aniLevel;
    private String titleImage;
    private boolean view;
    private int qCount;
}
