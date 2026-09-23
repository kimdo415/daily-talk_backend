package com.karainc.dailytalk.domain.behavior.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyBeData {
    private Long behavResultId;
    private List<String> behavFive;
    private List<String> behavStick;
    private String behavDate;
}
