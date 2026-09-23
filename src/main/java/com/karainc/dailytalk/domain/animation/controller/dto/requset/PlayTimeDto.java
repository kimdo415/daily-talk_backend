package com.karainc.dailytalk.domain.animation.controller.dto.requset;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayTimeDto {
    private Long aniIdx;
    private List<Integer> playTime;
    private List<Integer> rate;
}
