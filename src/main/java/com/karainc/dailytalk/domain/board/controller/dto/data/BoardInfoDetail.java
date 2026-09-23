package com.karainc.dailytalk.domain.board.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardInfoDetail {
    private long boardIdx;
    private String boardTitle;
    private String boardContent;
    private boolean replyStatus;
    private boolean boardStatus;
    private String writer;
    private String reply;
    private String date;
    private String name;
}
