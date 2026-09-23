package com.karainc.dailytalk.domain.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RehabListDto {
    // memberTable 조회
    private Long memberIdx;
    private String profile;
    private String name;

    // 재활사 테이블 조회
    private List<String> cert;
    private String division;
    private String day;
    private String phoneNumber;
    private String activityArea;
    private String status;
}
