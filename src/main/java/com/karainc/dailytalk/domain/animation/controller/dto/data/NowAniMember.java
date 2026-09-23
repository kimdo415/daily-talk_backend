package com.karainc.dailytalk.domain.animation.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NowAniMember {
    private String memberId;
    private String name;
    private String profile;
    private String date;
    private Integer playTime;
    private Integer replayCount;
    private Integer outCount;
    private String rate;
}
