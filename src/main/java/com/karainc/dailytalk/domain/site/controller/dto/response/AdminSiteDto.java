package com.karainc.dailytalk.domain.site.controller.dto.response;

import com.karainc.dailytalk.domain.site.controller.dto.data.SiteData;
import com.karainc.dailytalk.domain.site.controller.dto.requset.TermsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSiteDto {
    private String status;
    private String message;
    private SiteData data;
}
