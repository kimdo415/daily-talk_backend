package com.karainc.dailytalk.domain.animation.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AniInfoDetail {
    private long aniIdx;
    private long aniCategoryIdx;
    private String aniCategory;
    private String aniTitle;
    private String aniSubTitle;
    private String aniLevel;
    private String aniAge;
    private String titleImage;
    private List<AniDetail> qna;
}
