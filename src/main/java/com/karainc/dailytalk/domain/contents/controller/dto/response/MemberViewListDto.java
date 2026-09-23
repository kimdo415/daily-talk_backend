package com.karainc.dailytalk.domain.contents.controller.dto.response;


import com.karainc.dailytalk.domain.contents.controller.dto.data.MemberViewList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberViewListDto {
    private String status;
    private String message;
    private List<MemberViewList> data;
}
