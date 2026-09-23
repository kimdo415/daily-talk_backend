package com.karainc.dailytalk.domain.consult.controller;


import com.karainc.dailytalk.domain.consult.controller.dto.requset.ApplyDto;
import com.karainc.dailytalk.domain.consult.controller.dto.requset.CompleteAppDto;
import com.karainc.dailytalk.domain.consult.controller.dto.response.*;
import com.karainc.dailytalk.domain.consult.service.ConsultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/consult")
@RequiredArgsConstructor
public class ConsultController {
    private final ConsultService consultService;

    // 회원 비회원 전부 재활사 목록 보기
    @GetMapping
    public RehabListDto getReList(){
        return consultService.getRehabList();
    }

    // 결제 회원 한정 상담 신청하기
    @PostMapping("/apply")
    public ResultApplyDto getApply(@RequestHeader(value = "X-MEMBER-TOKEN" ,required = false)String memberKey,
                                   @RequestBody ApplyDto applyDto){
        return consultService.apply(memberKey,applyDto);
    }

    //내 상담 내역 보기
    //결제를 했다가 그만둬도 내용은 조회 가능해야함
    @GetMapping("/member-apply")
    public MyApplyResultDto getMyApply(@RequestHeader(value = "X-MEMBER-TOKEN" ,required = false)String memberKey){
        return consultService.getMyApply(memberKey);
    }

    //어드민단 상담 내역 조회
    @GetMapping("/list-admin")
    public AdminAppDto getAllList(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey){
        return consultService.getAdminApply(adminKey);
    }

    @GetMapping("/consult-detail")
    public  DetailDto getDetail(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                @RequestParam(value = "consultIdx")Long consultIdx){
        return consultService.getConsultDetail(adminKey,consultIdx);
    }

    //어드민권한으로 상담 완료 하기
    @PutMapping("/connect")
    public MsgDto connect(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                          @RequestBody CompleteAppDto completeAppDto){
        return consultService.completeConsult(adminKey,completeAppDto);
    }

}
