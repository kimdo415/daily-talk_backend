package com.karainc.dailytalk.domain.animation.controller.dto.requset;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AniSetDto {
    private long aniIdx;
    private String aniSubTitle;
    private String aniLevel;
    private String aniAge;
}
