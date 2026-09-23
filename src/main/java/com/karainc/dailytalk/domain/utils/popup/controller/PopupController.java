package com.karainc.dailytalk.domain.utils.popup.controller;

import com.karainc.dailytalk.domain.utils.popup.controller.dto.response.AdminPopDto;
import com.karainc.dailytalk.domain.utils.popup.controller.dto.response.PopupMsgDto;
import com.karainc.dailytalk.domain.utils.popup.controller.dto.response.UserPopDto;
import com.karainc.dailytalk.domain.utils.popup.service.PopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/popup")
@RequiredArgsConstructor
public class PopupController {

    private final PopupService popupService;

    @PostMapping("/make-popup")
    public PopupMsgDto make(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                            @RequestParam(value = "popupName",required = false)String popupName,
                            @RequestParam(value = "popupDate", required = false)String popupDate,
                            @RequestParam(value = "popupStatus" ,required = false)Boolean popupStatus,
                            @RequestParam(value = "popupLink",required = false)String popupLink,
                            @RequestParam(value = "popupImage",required = false)MultipartFile popupImage){

        return popupService.createPop(adminKey,popupName,popupStatus,popupDate,popupLink,popupImage);
    }

    @PutMapping("/change-popup")
    public PopupMsgDto updateP(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                              @RequestParam(value = "popupName",required = false)String popupName,
                              @RequestParam(value = "popupDate", required = false)String popupDate,
                              @RequestParam(value = "popupStatus" ,required = false)Boolean popupStatus,
                              @RequestParam(value = "popupLink",required = false)String popupLink,
                              @RequestParam(value = "popupImage",required = false)MultipartFile popupImage,
                              @RequestParam(value = "delete",required = false)Boolean delete){

        return popupService.changePop(adminKey,popupName,popupStatus,popupDate,popupLink,popupImage,delete);
    }

    @GetMapping("/admin-view")
    public AdminPopDto get(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return popupService.forAdmin(adminKey);
    }

    @GetMapping
    public UserPopDto userPop(){
        return popupService.forUser();
    }
}
