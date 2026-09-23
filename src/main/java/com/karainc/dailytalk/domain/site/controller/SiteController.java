package com.karainc.dailytalk.domain.site.controller;


import com.karainc.dailytalk.domain.site.controller.dto.requset.TermsDto;
import com.karainc.dailytalk.domain.site.controller.dto.response.AdminSiteDto;
import com.karainc.dailytalk.domain.site.controller.dto.response.SiteMsgDto;
import com.karainc.dailytalk.domain.site.controller.dto.response.UserSiteDto;
import com.karainc.dailytalk.domain.site.service.SiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/site")
@RequiredArgsConstructor
public class SiteController {
    private final SiteService siteService;

    @PostMapping("/make-terms")
    public SiteMsgDto make(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                           @RequestBody TermsDto termsDto){
        return siteService.makeTerms(adminKey,termsDto);
    }

    @PutMapping("/change-terms")
    public SiteMsgDto update(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                             @RequestBody TermsDto termsDto){
        return siteService.updateTerms(adminKey,termsDto);
    }

    @GetMapping("/admin-view")
    public AdminSiteDto viewAdmin(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return siteService.forAdmin(adminKey);
    }

    @GetMapping
    public UserSiteDto viewUser(){
        return siteService.forUser();
    }


}
