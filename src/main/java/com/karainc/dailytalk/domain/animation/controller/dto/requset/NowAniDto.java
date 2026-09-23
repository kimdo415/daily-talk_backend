package com.karainc.dailytalk.domain.animation.controller.dto.requset;


import com.karainc.dailytalk.domain.animation.controller.dto.data.NowAniMember;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NowAniDto {
    private String status;
    private String message;
    private List<NowAniMember> data;
}
