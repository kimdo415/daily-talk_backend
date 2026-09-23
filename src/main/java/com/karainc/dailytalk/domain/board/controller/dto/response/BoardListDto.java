package com.karainc.dailytalk.domain.board.controller.dto.response;

import com.karainc.dailytalk.domain.board.controller.dto.data.BoardList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BoardListDto {
    private String status;
    private String message;
    private List<BoardList> data;
}
