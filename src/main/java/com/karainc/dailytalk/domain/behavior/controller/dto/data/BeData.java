package com.karainc.dailytalk.domain.behavior.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BeData {

    private List<String> behaviors;
    private List<String> behaviorsEnum;
    private String duringDate;
    private String limitDate;
 }
