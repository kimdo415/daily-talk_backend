package com.karainc.dailytalk.domain.contents.controller.dto.response;


import com.karainc.dailytalk.domain.contents.controller.dto.data.ContentsId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentsMakeDto {
    private String status;
    private String message;
    private ContentsId data;
}
