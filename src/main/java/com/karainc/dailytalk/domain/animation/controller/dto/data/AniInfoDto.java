package com.karainc.dailytalk.domain.animation.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AniInfoDto {
    private long aniIdx;
    private long aniCategoryIdx;
    private String aniCategory;
    private int qCount;
    private String aniTitle;
    private String titleImage;
    private String aniSubTitle;
    private String aniAge;
    private String aniLevel;
    private Boolean view;
}
