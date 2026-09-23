package com.karainc.dailytalk.domain.contents.controller.dto.response;

import com.karainc.dailytalk.domain.contents.controller.dto.data.MemberConInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCtInfoDto {
    private String status;
    private String message;
    private MemberConInfo data;
}
