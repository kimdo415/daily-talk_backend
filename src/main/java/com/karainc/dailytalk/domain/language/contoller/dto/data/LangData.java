package com.karainc.dailytalk.domain.language.contoller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LangData {


    private long langResultId;
    private String langPoint;
    private List<String> langStick;
    private String langDate;


}
