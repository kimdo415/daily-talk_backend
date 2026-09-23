package com.karainc.dailytalk.domain.notice.controller.dto.response;


import com.karainc.dailytalk.domain.notice.controller.dto.data.NoticeListDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoteListDto {
    private String status;
    private String message;
    private List<NoticeListDto> data;
}
