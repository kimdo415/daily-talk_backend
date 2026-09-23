package com.karainc.dailytalk.domain.dashboard.controller;

import com.karainc.dailytalk.domain.dashboard.controller.dto.requset.DashDto;
import com.karainc.dailytalk.domain.dashboard.controller.dto.requset.DashPayDto;
import com.karainc.dailytalk.domain.dashboard.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dash")
@RequiredArgsConstructor
public class DashBoardController {
    private final DashBoardService dashBoardService;

    @GetMapping
    public DashDto getDash(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey){
        return dashBoardService.getDashboard(adminKey);
    }

    @GetMapping("/pay")
    public DashPayDto getDashPay(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return dashBoardService.getDashPay(adminKey);
    }

    @GetMapping("/random")
    public void randomCreate(){
        dashBoardService.getRandom();
    }
}
