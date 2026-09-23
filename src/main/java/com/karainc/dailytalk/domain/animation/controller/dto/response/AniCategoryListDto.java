package com.karainc.dailytalk.domain.animation.controller.dto.response;

import com.karainc.dailytalk.domain.animation.entity.AniCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AniCategoryListDto {
    private String status;
    private String message;
    private List<AniCategory> data;
}
