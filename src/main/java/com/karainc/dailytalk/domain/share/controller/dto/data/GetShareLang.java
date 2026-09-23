package com.karainc.dailytalk.domain.share.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetShareLang {
    private Long langResultId;
    private String name;
    private String langPoint;
    private List<String> langStick;
    private String langDate;
}
