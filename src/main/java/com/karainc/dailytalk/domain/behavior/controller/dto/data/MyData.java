package com.karainc.dailytalk.domain.behavior.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyData {
    private List<MyBeData> myData;
    private String nextData;
}
