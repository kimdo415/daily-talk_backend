package com.karainc.dailytalk.domain.notice.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoteDetail {
    private long noticeIdx;
    private String noticeTitle;
    private String thumbnail;
    private String content;
    private String type;
    private String date;
}
