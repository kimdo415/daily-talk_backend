package com.karainc.dailytalk.domain.notice.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberNote {
    private long noticeIdx;
    private String noticeTitle;
    private String thumbNail;
    private String content;
    private String type;
    private String date;
}
