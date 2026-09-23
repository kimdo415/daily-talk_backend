package com.karainc.dailytalk.domain.share.controller.dto.response;


import com.karainc.dailytalk.domain.share.controller.dto.data.GetShareBehav;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBehavDto {
    private String status;
    private String message;
    private GetShareBehav data;
}
