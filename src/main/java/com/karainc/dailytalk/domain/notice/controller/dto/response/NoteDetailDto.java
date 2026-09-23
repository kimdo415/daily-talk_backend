package com.karainc.dailytalk.domain.notice.controller.dto.response;


import com.karainc.dailytalk.domain.notice.controller.dto.data.NoteDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDetailDto {
    private String status;
    private String message;
    private NoteDetail data;
}
