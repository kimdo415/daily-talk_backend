package com.karainc.dailytalk.domain.utils.banner.controller;

import com.karainc.dailytalk.domain.utils.banner.controller.dto.request.BannerDto;
import com.karainc.dailytalk.domain.utils.banner.controller.dto.response.AdminBannerDto;
import com.karainc.dailytalk.domain.utils.banner.controller.dto.response.BannerMsgDto;
import com.karainc.dailytalk.domain.utils.banner.controller.dto.response.UserBannerDto;
import com.karainc.dailytalk.domain.utils.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @PostMapping("/make-banner")
    public BannerMsgDto make(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                             @RequestBody BannerDto bannerDto){
        return bannerService.banner(adminKey,bannerDto);
    }

    @PutMapping("/change-banner")
    public BannerMsgDto updateB(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                             @RequestBody BannerDto bannerDto){
        return bannerService.updateBanner(adminKey,bannerDto);
    }

    @GetMapping("/admin-view")
    public AdminBannerDto getNowBanner(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return bannerService.getBanner(adminKey);
    }


    @PostMapping("/video")
    public BannerMsgDto setVideo(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                 @RequestParam(value = "videoLink",required = false)MultipartFile videoLink){
        return bannerService.setUpVideo(adminKey,videoLink);
    }

    @GetMapping("/admin-view/video")
    public AdminBannerDto getV(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return bannerService.getVideLink(adminKey);
    }

    @DeleteMapping("/cut-link")
    public BannerMsgDto cutLink(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return bannerService.deleteVideo(adminKey);
    }

    @GetMapping
    public UserBannerDto userBanner(){
        return bannerService.getUserBanner();
    }
}
