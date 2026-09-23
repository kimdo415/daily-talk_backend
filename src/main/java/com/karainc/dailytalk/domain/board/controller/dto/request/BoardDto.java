package com.karainc.dailytalk.domain.board.controller.dto.request;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {
    private String boardTitle;
    private String boardContent;
    private Boolean boardStatus;
}
