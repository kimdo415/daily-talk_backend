package com.karainc.dailytalk.domain.board.controller.dto.response;


import com.karainc.dailytalk.domain.board.controller.dto.data.BoardInfoDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardInfoDto {
    public String status;
    private String message;
    private BoardInfoDetail data;
}
