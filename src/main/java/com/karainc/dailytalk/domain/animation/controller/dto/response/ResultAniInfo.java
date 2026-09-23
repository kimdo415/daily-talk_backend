package com.karainc.dailytalk.domain.animation.controller.dto.response;


import com.karainc.dailytalk.domain.animation.controller.dto.data.AniInfoDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultAniInfo {
    private String status;
    private String message;
    private AniInfoDetail data;
}
