package com.karainc.dailytalk.domain.utils.popup.service;

import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.utils.popup.controller.dto.data.PopupData;
import com.karainc.dailytalk.domain.utils.popup.controller.dto.response.AdminPopDto;
import com.karainc.dailytalk.domain.utils.popup.controller.dto.response.PopupMsgDto;
import com.karainc.dailytalk.domain.utils.popup.controller.dto.response.UserPopDto;
import com.karainc.dailytalk.domain.utils.popup.entity.Popup;
import com.karainc.dailytalk.domain.utils.popup.repository.PopupRepository;
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
public class PopupService {
    private final AdminService adminService;
    private final PopupRepository popupRepository;
    private final S3UploadService s3UploadService;


    public PopupMsgDto createPop(String adminKey, String popupName, Boolean popupStatus,
                                 String popupDate, String popupLink, MultipartFile popupImage){
        adminService.checkAdmin(adminKey);
        if(popupName.isEmpty() || popupDate.isEmpty() || popupLink.isEmpty()){
            throw new NoDataExceptionHandler("양식을 모두 작성해주세요");
        }

        if(popupStatus==null){
            throw new NoDataExceptionHandler("출력 유무를 설정해 주세요");
        }

        String popImage;

        if(popupImage==null || popupImage.isEmpty()){
            popImage=null;
        }else if(popupImage.getSize()>30*30*1024){
            throw new DataNotMatchHandler("30MB 크기는 업로드 할 수 없습니다");
        }else {
            log.info("팝업 이미지 생성");
            String fileExtension= popupImage.getOriginalFilename().substring(popupImage.getOriginalFilename().lastIndexOf("."));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date()) + fileExtension;
            popImage=s3UploadService.uploadImage(popupImage,"popup/"+newFileName);
        }

        Popup popup = Popup.builder().
                popupName(popupName).
                popupStatus(popupStatus).
                popupDate(popupDate).
                popupLink(popupLink).
                popupImage(popImage).
                build();
        popupRepository.save(popup);

        PopupMsgDto popupMsgDto = new PopupMsgDto();
        popupMsgDto.setStatus("200");
        popupMsgDto.setMessage("팝업 설정 완료 : 관리자 권한");
        popupMsgDto.setData("팝업 설정 완료 : 관리자 권한");

        return popupMsgDto;
    }


    public PopupMsgDto changePop(String adminKey, String popupName, Boolean popupStatus,
                                 String popupDate, String popupLink, MultipartFile popupImage, Boolean delete){
        adminService.checkAdmin(adminKey);

        long popId= 1;
        Popup popup = popupRepository.findByPopupId(popId);
        if(popup == null){
            throw new NoDataExceptionHandler("먼저 팝업 설정을 완료해주세요");
        }

        String popName;
        if(popupName==null || popupName.isEmpty()){
            popName = popup.getPopupName();
        }else{
            popName = popupName;
        }

        Boolean popStatus;
        if(popupStatus==null){
            popStatus = popup.getPopupStatus();
        }else{
            popStatus = popupStatus;
        }

        String popDate;
        if(popupDate==null ||popupDate.isEmpty()){
            popDate = popup.getPopupDate();
        }else{
            popDate = popupDate;
        }

        String popLink;
        if(popupLink==null || popupLink.isEmpty()){
            popLink = popup.getPopupLink();
        }else{
            popLink = popupLink;
        }

        String popImage;

        if(delete==Boolean.FALSE){
            if(popupImage==null || popupImage.isEmpty()){
                log.info("이미지 유지");
                popImage=popup.getPopupImage();
            }else{
                if(popupImage.getSize()>30*30*1024) {
                    throw new DataNotMatchHandler("30MB 크기는 업로드 할 수 없습니다");
                }
                log.info("이미지 변경");
                String fileExtension= popupImage.getOriginalFilename().substring(popupImage.getOriginalFilename().lastIndexOf("."));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String newFileName = dateFormat.format(new Date()) + fileExtension;
                popImage=s3UploadService.uploadImage(popupImage,"popup/"+newFileName);
            }
        }else{
            log.info("이미지 삭제");
            popImage=null;
        }


        popup.setPopupName(popName);
        popup.setPopupStatus(popStatus);
        popup.setPopupLink(popLink);
        popup.setPopupDate(popDate);
        popup.setPopupImage(popImage);

        popupRepository.save(popup);

        PopupMsgDto popupMsgDto = new PopupMsgDto();
        popupMsgDto.setStatus("200");
        popupMsgDto.setMessage("팝업 수정 완료 : 관리자 권한");
        popupMsgDto.setData("팝업 수정 완료 : 관리자 권한");

        return popupMsgDto;
    }


    public AdminPopDto forAdmin(String adminKey){
        adminService.checkAdmin(adminKey);
        long popId=1;
        Popup popup = popupRepository.findByPopupId(popId);
        if(popup == null){
            throw new NoDataExceptionHandler("팝업을 먼저 설정해주세요");
        }

        PopupData popupData = new PopupData();
        popupData.setPopupName(popup.getPopupName());
        popupData.setPopupState(popup.getPopupStatus());
        popupData.setPopupDate(popup.getPopupDate());
        popupData.setPopupLink(popup.getPopupLink());
        popupData.setPopupImage(popup.getPopupImage());

        AdminPopDto adminPopDto = new AdminPopDto();
        adminPopDto.setStatus("200");
        adminPopDto.setMessage("팝업 정보 : 관리자 권한");
        adminPopDto.setData(popupData);

        return adminPopDto;
    }

    public UserPopDto forUser(){

        long popId=1;
        Popup popup = popupRepository.findByPopupId(popId);
        if(popup == null || popup.getPopupStatus()==Boolean.FALSE){
            throw new NoDataExceptionHandler("현재 팝업 준비중입니다");
        }

        PopupData popupData = new PopupData();
        popupData.setPopupName(popup.getPopupName());
        popupData.setPopupState(popup.getPopupStatus());
        popupData.setPopupDate(popup.getPopupDate());
        popupData.setPopupLink(popup.getPopupLink());
        popupData.setPopupImage(popup.getPopupImage());

        UserPopDto userPopDto = new UserPopDto();
        userPopDto.setStatus("200");
        userPopDto.setMessage("팝업 정보");
        userPopDto.setData(popupData);
        return userPopDto;
    }
}
