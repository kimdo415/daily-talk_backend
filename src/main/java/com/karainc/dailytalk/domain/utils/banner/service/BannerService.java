package com.karainc.dailytalk.domain.utils.banner.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.site.repository.SiteRepository;
import com.karainc.dailytalk.domain.utils.banner.controller.dto.data.BannerData;
import com.karainc.dailytalk.domain.utils.banner.controller.dto.request.BannerDto;
import com.karainc.dailytalk.domain.utils.banner.controller.dto.response.AdminBannerDto;
import com.karainc.dailytalk.domain.utils.banner.controller.dto.response.BannerMsgDto;
import com.karainc.dailytalk.domain.utils.banner.controller.dto.response.UserBannerDto;
import com.karainc.dailytalk.domain.utils.banner.entity.Banner;
import com.karainc.dailytalk.domain.utils.banner.repository.BannerRepository;
import com.karainc.dailytalk.domain.utils.s3.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Service
@Slf4j
public class BannerService {

    private final AdminService adminService;
    private final BannerRepository bannerRepository;
    private final S3UploadService s3UploadService;


    public BannerMsgDto banner(String adminKey, BannerDto bannerDto){
        adminService.checkAdmin(adminKey);

        if(bannerDto.getBannerLink()==null || bannerDto.getBannerLink().isEmpty()){
            throw new NoDataExceptionHandler("배너설정을 완료해주세요");
        }


        Banner banner = Banner.builder()
                .bannerLink(bannerDto.getBannerLink())
                .build();

        bannerRepository.save(banner);

        log.info("배너 설정 링크 주소 (관리자 권한)");
        log.info(bannerDto.getBannerLink());

        BannerMsgDto bannerMsgDto = new BannerMsgDto();
        bannerMsgDto.setStatus("200");
        bannerMsgDto.setMessage("배너 설정 완료 (관리자 권한)");
        bannerMsgDto.setData("배너 설정 완료 (관리자 권한)");

        return bannerMsgDto;
    }

    public BannerMsgDto updateBanner(String adminKey,BannerDto bannerDto){
        adminService.checkAdmin(adminKey);

        long i =1;
        Banner banner = bannerRepository.findByBannerId(i);
        if(banner==null){
            throw new NoDataExceptionHandler("배너를 먼저 설정해주세요");
        }

        String msg;
        if(bannerDto.getBannerLink()==null || bannerDto.getBannerLink().isEmpty()){
            msg="배너 삭제";
        }else {
            msg = banner.getBannerLink();
        }

        banner.setBannerLink(bannerDto.getBannerLink());
        bannerRepository.save(banner);

        log.info("배너 수정 (관리자 권한)");
        log.info(msg);

        BannerMsgDto bannerMsgDto = new BannerMsgDto();
        bannerMsgDto.setStatus("200");
        bannerMsgDto.setMessage("배너 수정 완료 (관리자 권한)");
        bannerMsgDto.setData("배너 수정 완료 (관리자 권한)");

        return bannerMsgDto;
    }

    public AdminBannerDto getBanner(String adminKey){
        adminService.checkAdmin(adminKey);

        long i =1;
        Banner banner = bannerRepository.findByBannerId(i);
        if(banner==null){
            throw new NoDataExceptionHandler("배너를 먼저 설정해주세요");
        }

        banner.setBannerLink(banner.getBannerLink());
        bannerRepository.save(banner);



        AdminBannerDto adminBannerDto = new AdminBannerDto();
        adminBannerDto.setStatus("200");
        adminBannerDto.setMessage("현재 배너 주소 (관리자 권한)");
        adminBannerDto.setData(banner.getBannerLink());

        return adminBannerDto;
    }

    public AdminBannerDto getVideLink(String adminKey){
        adminService.checkAdmin(adminKey);

        long i =1;
        Banner banner = bannerRepository.findByBannerId(i);


        if(banner==null){
            throw new NoDataExceptionHandler("등록된 메인배너가 없습니다");
        }

        AdminBannerDto adminBannerDto = new AdminBannerDto();
        adminBannerDto.setStatus("200");
        adminBannerDto.setMessage("현재 메인배너 주소 (관리자 권한)");
        adminBannerDto.setData(banner.getVideoLink());

        return adminBannerDto;
    }



    public BannerMsgDto setUpVideo(String adminKey,MultipartFile videoLink){
        adminService.checkAdmin(adminKey);

        long id =1;
        Banner banner =bannerRepository.findByBannerId(id);

        if(banner==null){
            throw new DataNotMatchHandler("배너 설정을 먼저 해주세요");
        }
        String getLink;

        if(videoLink == null || videoLink.isEmpty()){
            log.info("기존 영상 저장");
            getLink=banner.getVideoLink();
        }else{
            System.out.println(videoLink.getOriginalFilename());
            System.out.println(videoLink.getSize());
            if(videoLink.getSize()>30000000) {
                throw new DataNotMatchHandler("용량 초과 : 최대 30MB 까지 업로드가 가능합니다");
            }
            String fileExtension = videoLink.getOriginalFilename().substring(videoLink.getOriginalFilename().lastIndexOf("."));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date())+fileExtension;
            getLink =s3UploadService.uploadImage(videoLink,"video/"+newFileName);
        }

        System.out.println(getLink);
        banner.setVideoLink(getLink);
        bannerRepository.save(banner);


        BannerMsgDto bannerMsgDto = new BannerMsgDto();
        bannerMsgDto.setStatus("200");
        bannerMsgDto.setMessage("메인 배너 수정 완료 (관리자 권한)");
        bannerMsgDto.setData("메인 배너 수정 완료 (관리자 권한)");
        return bannerMsgDto;
    }

    public BannerMsgDto deleteVideo(String adminKey){
        adminService.checkAdmin(adminKey);

        long id =1;
        Banner banner =bannerRepository.findByBannerId(id);
        if(banner==null){
            throw new NoDataExceptionHandler("배너를 먼저 설정해 주세요");
        }

        s3UploadService.deleteFolder("video/");
        banner.setVideoLink(null);
        bannerRepository.save(banner);
        BannerMsgDto bannerMsgDto = new BannerMsgDto();
        bannerMsgDto.setStatus("200");
        bannerMsgDto.setMessage("메인 배너 삭제 완료 (관리자 권한)");
        bannerMsgDto.setData("메인 배너 삭제 완료 (관리자 권한)");
        return bannerMsgDto;
    }


    public UserBannerDto getUserBanner(){

        long i =1;
        Banner banner = bannerRepository.findByBannerId(i);
        if(banner==null){
            throw new NoDataExceptionHandler("현재 배너 설정 중입니다");
        }



        BannerData bannerData = new BannerData();
        bannerData.setBannerLink(banner.getBannerLink());
        bannerData.setVideoLink(banner.getVideoLink());

        UserBannerDto userBannerDto = new UserBannerDto();
        userBannerDto.setStatus("200");
        userBannerDto.setMessage("현재 배너 주소");
        userBannerDto.setData(bannerData);
        return userBannerDto;
    }
}
