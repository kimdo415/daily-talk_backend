package com.karainc.dailytalk.domain.board.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBoardDto {
    private long boardIdx;
    private String boardTitle;
    private String boardContent;
    private Boolean boardStatus;
}
