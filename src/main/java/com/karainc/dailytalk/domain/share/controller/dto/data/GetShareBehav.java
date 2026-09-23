package com.karainc.dailytalk.domain.share.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetShareBehav {
    private Long behavResultId;
    private String name;
    private List<String> behavFive;
    private List<String> behavStick;
    private String behavDate;
}
