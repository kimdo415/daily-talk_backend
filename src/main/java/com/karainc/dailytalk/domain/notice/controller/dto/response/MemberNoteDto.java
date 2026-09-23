package com.karainc.dailytalk.domain.notice.controller.dto.response;


import com.karainc.dailytalk.domain.notice.controller.dto.data.MemberNote;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberNoteDto {
    private String status;
    private String message;
    private List<MemberNote> data;
}
