package com.karainc.dailytalk.domain.notice.controller.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDto {
    private Long noticeIdx;
    private String noticeTitle;
    private String content;
    private String type;
}
