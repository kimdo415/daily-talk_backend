package com.karainc.dailytalk.domain.site.controller.dto.requset;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TermsDto {
    private String instagram;
    private String youtube;
    private String naver;
    private String facebook;
    private String siteName;
    private String info;
    private String tos;
    private String privacy;
    private String children;
    private String pledge;
    private String marketing;
    private String payAgree;
}
