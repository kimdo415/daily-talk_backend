package com.karainc.dailytalk.domain.behavior.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllData {
    private String name;
    private String profile;
    private List<String> behavFive;
    private List<String> behavStick;
    private String behavDate;
    private String nextDate;
}
