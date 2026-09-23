package com.karainc.dailytalk.domain.contents.controller.dto.response;


import com.karainc.dailytalk.domain.contents.controller.dto.data.TotalContents;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentsInfoDto {
    private String status;
    private String message;
    private TotalContents data;
}
