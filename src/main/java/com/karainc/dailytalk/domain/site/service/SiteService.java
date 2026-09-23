package com.karainc.dailytalk.domain.site.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.site.controller.dto.data.SiteData;
import com.karainc.dailytalk.domain.site.controller.dto.requset.TermsDto;
import com.karainc.dailytalk.domain.site.controller.dto.response.AdminSiteDto;
import com.karainc.dailytalk.domain.site.controller.dto.response.SiteMsgDto;
import com.karainc.dailytalk.domain.site.controller.dto.response.UserSiteDto;
import com.karainc.dailytalk.domain.site.entity.Site;
import com.karainc.dailytalk.domain.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class SiteService {
    private final AdminService adminService;
    private final SiteRepository siteRepository;

    public SiteMsgDto makeTerms(String adminKey, TermsDto termsDto){
        adminService.checkAdmin(adminKey);
        Site site = Site.builder().
                instagram(termsDto.getInstagram()).
                youtube(termsDto.getYoutube()).
                naver(termsDto.getNaver()).
                facebook(termsDto.getFacebook()).
                siteName(termsDto.getSiteName()).
                info(termsDto.getInfo()).
                tos(termsDto.getTos()).
                privacy(termsDto.getPrivacy()).
                children(termsDto.getChildren()).
                pledge(termsDto.getPledge()).
                marketing(termsDto.getMarketing()).
                payAgree(termsDto.getPayAgree()).
                build();

        siteRepository.save(site);
        log.info("사이트 정책 설정 : (관리자 권한)");

        SiteMsgDto siteMsgDto = new SiteMsgDto();
        siteMsgDto.setStatus("200");
        siteMsgDto.setMessage("사이트 정책 설정 완료: 관리자 권한");
        siteMsgDto.setData("사이트 정책 설정 완료: 관리자 권한");

        return siteMsgDto;
    }

    public SiteMsgDto updateTerms(String adminKey, TermsDto termsDto){
        adminService.checkAdmin(adminKey);
        long i=1;
        Site site = siteRepository.findBySiteId(i);
        if(site == null){
            throw new NoDataExceptionHandler("먼저 약관을 설정해 주세요");
        }

        String inst;
        if(termsDto.getInstagram()==null || termsDto.getInstagram().isEmpty() ){
            inst = site.getInstagram();
        }else{
            inst = termsDto.getInstagram();
        }

        String yt;
        if(termsDto.getYoutube()==null || termsDto.getYoutube().isEmpty()){
            yt=site.getYoutube();
        }else{
            yt=termsDto.getYoutube();
        }

        String nav;
        if(termsDto.getNaver()==null || termsDto.getNaver().isEmpty()){
            nav=site.getNaver();
        }else{
            nav=termsDto.getNaver();
        }

        String siteN;
        if(termsDto.getSiteName()==null || termsDto.getSiteName().isEmpty()){
            siteN=site.getSiteName();
        }else{
            siteN=termsDto.getSiteName();
        }

        String fb;
        if(termsDto.getFacebook()==null || termsDto.getFacebook().isEmpty()){
            fb=site.getFacebook();
        }else{
            fb=termsDto.getFacebook();
        }

        String info;
        if(termsDto.getInfo()==null || termsDto.getInfo().isEmpty()){
            info=site.getInfo();
        }else{
            info= termsDto.getInfo();
        }

        String tos;
        if(termsDto.getTos()==null || termsDto.getTos().isEmpty()){
            tos= site.getTos();
        }else{
            tos=termsDto.getTos();
        }

        String privacy;
        if(termsDto.getPrivacy()==null || termsDto.getPrivacy().isEmpty()){
            privacy = site.getPrivacy();
        }else{
            privacy = termsDto.getPrivacy();
        }

        String children;
        if(termsDto.getChildren()==null || termsDto.getChildren().isEmpty()){
            children=site.getChildren();
        }else {
            children= termsDto.getChildren();
        }

        String pledge;
        if(termsDto.getPledge()==null || termsDto.getPledge().isEmpty()){
            pledge=site.getPledge();
        }else {
            pledge= termsDto.getPledge();
        }

        String mk;
        if(termsDto.getMarketing()==null || termsDto.getMarketing().isEmpty()){
            mk=site.getMarketing();
        }else {
            mk=termsDto.getMarketing();
        }

        String payAgree;
        if(termsDto.getPayAgree()==null || termsDto.getPayAgree().isEmpty()){
            payAgree=site.getPayAgree();
        }else{
            payAgree=termsDto.getPayAgree();
        }

        site.setInstagram(inst);
        site.setYoutube(yt);
        site.setSiteName(siteN);
        site.setFacebook(fb);
        site.setNaver(nav);
        site.setInfo(info);
        site.setTos(tos);
        site.setPrivacy(privacy);
        site.setChildren(children);
        site.setPledge(pledge);
        site.setMarketing(mk);
        site.setPayAgree(payAgree);

        siteRepository.save(site);
        log.info("사이트 정책 수정 : (관리자 권한)");

        SiteMsgDto siteMsgDto = new SiteMsgDto();
        siteMsgDto.setStatus("200");
        siteMsgDto.setMessage("사이트 정책 수정 완료: 관리자 권한");
        siteMsgDto.setData("사이트 정책 수정 완료: 관리자 권한");

        return siteMsgDto;
    }
    public AdminSiteDto forAdmin(String adminKey){
        adminService.checkAdmin(adminKey);
        long i=1;
        Site site = siteRepository.findBySiteId(i);
        if(site == null){
            throw new NoDataExceptionHandler("먼저 약관을 설정해 주세요");
        }
        SiteData siteData = new SiteData();
        siteData.setInstagram(site.getInstagram());
        siteData.setYoutube(site.getYoutube());
        siteData.setNaver(site.getNaver());
        siteData.setFacebook(site.getFacebook());
        siteData.setSiteName(site.getSiteName());
        siteData.setInfo(site.getInfo());
        siteData.setTos(site.getTos());
        siteData.setPrivacy(site.getPrivacy());
        siteData.setChildren(site.getChildren());
        siteData.setPledge(site.getPledge());
        siteData.setMarketing(site.getMarketing());
        siteData.setPayAgree(site.getPayAgree());

        AdminSiteDto adminSiteDto =new AdminSiteDto();
        adminSiteDto.setStatus("200");
        adminSiteDto.setMessage("현재 사이트 설정 및 약관 정보 : (관리자 권한)");
        adminSiteDto.setData(siteData);

        return adminSiteDto;
    }



    public UserSiteDto forUser(){
        long i=1;
        Site site = siteRepository.findBySiteId(i);
        if(site == null){
            throw new NoDataExceptionHandler("현재 사이트 약관 준비중 입니다");
        }
        SiteData siteData = new SiteData();
        siteData.setInstagram(site.getInstagram());
        siteData.setYoutube(site.getYoutube());
        siteData.setNaver(site.getNaver());
        siteData.setFacebook(site.getFacebook());
        siteData.setSiteName(site.getSiteName());
        siteData.setInfo(site.getInfo());
        siteData.setTos(site.getTos());
        siteData.setPrivacy(site.getPrivacy());
        siteData.setChildren(site.getChildren());
        siteData.setPledge(site.getPledge());
        siteData.setMarketing(site.getMarketing());
        siteData.setPayAgree(site.getPayAgree());

        UserSiteDto userSiteDto = new UserSiteDto();
        userSiteDto.setStatus("200");
        userSiteDto.setMessage("사이트 약관 조회");
        userSiteDto.setData(siteData);
        return userSiteDto;
    }


}
