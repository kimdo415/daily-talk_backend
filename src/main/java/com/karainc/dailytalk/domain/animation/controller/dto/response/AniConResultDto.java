package com.karainc.dailytalk.domain.animation.controller.dto.response;


import com.karainc.dailytalk.domain.animation.controller.dto.data.MyContentsInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AniConResultDto {
    private String status;
    private String message;
    private List<MyContentsInfo> data;
}
