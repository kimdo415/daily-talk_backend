package com.karainc.dailytalk.domain.contents.controller.dto.response;


import com.karainc.dailytalk.domain.contents.controller.dto.data.ContentList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ContentsListDto {
    private String status;
    private String message;
    private List<ContentList> data;
}
