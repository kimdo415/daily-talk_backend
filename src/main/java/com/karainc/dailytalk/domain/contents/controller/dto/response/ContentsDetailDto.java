package com.karainc.dailytalk.domain.contents.controller.dto.response;

import com.karainc.dailytalk.domain.contents.entity.Contents;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentsDetailDto {
    private String status;
    private String message;
    private Contents data;
}
