package com.karainc.dailytalk.domain.board.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyDto {
    private Long boardIdx;
    public String reply;
}
