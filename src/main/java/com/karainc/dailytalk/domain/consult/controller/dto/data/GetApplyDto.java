package com.karainc.dailytalk.domain.consult.controller.dto.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetApplyDto {
    private Long consultIdx;
    private Long rehabIdx;
    private String rehabProfile;
    private String rehabName;
    private Long memberIdx;
    private String memberProfile;
    private String memberName;
    private String startDate;
    private String endDate;
    private boolean status;
}
