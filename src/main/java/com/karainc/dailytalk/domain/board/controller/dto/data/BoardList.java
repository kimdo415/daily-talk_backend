package com.karainc.dailytalk.domain.board.controller.dto.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardList {
    private long boardIdx;
    private String boardTitle;
    private boolean boardStatus;
    private boolean replyStatus;
    private String writer;
    private String date;
}
