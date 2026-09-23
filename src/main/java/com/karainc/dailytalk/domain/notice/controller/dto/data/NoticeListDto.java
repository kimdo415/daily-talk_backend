package com.karainc.dailytalk.domain.notice.controller.dto.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeListDto {
    private long noticeIdx;
    private String noticeTitle;
    private String thumbNail;
    private String type;
    private boolean view;
    private String date;
}
