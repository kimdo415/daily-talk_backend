package com.karainc.dailytalk.domain.contents.controller.dto.response;


import com.karainc.dailytalk.domain.contents.controller.dto.data.ContentInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminConInfo {
    private String status;
    private String message;
    private ContentInfo data;
}
