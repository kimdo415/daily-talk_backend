package com.karainc.dailytalk.domain.animation.controller.dto.requset;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AniUpdateDto {
    private long aniIdx;
    private int aniNumber;
    private String animations;
    private String aniName;
}
