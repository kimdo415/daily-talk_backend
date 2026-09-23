package com.karainc.dailytalk.domain.utils.banner.controller.dto.response;


import com.karainc.dailytalk.domain.utils.banner.controller.dto.data.BannerData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBannerDto {
    private String status;
    private String message;
    private BannerData data;
}
