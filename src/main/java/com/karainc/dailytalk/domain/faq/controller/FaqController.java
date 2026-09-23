package com.karainc.dailytalk.domain.faq.controller;


import com.karainc.dailytalk.domain.faq.controller.dto.request.FaqBoardDto;
import com.karainc.dailytalk.domain.faq.controller.dto.request.FaqUpdateDto;
import com.karainc.dailytalk.domain.faq.controller.dto.request.StatusDto;
import com.karainc.dailytalk.domain.faq.controller.dto.response.AllFaqDto;
import com.karainc.dailytalk.domain.faq.controller.dto.response.FaqResultDto;
import com.karainc.dailytalk.domain.faq.entity.Faq;
import com.karainc.dailytalk.domain.faq.service.FaqService;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;


    // 어드민 권한으로 faq 페이지 생성
    @PostMapping("/page")
    public FaqResultDto createFaq(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                  @RequestBody FaqBoardDto faqBoardDto){
        return faqService.createFaq(adminKey,faqBoardDto);
    }


    // 어드민 권한으로 faq 페이지 수정
    @PutMapping("/change")
    public FaqResultDto updateFaq(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                  @RequestBody FaqUpdateDto faqUpdateDto){
        return faqService.updateFaq(adminKey,faqUpdateDto);
    }


    //어드민 권한으로 faq 페이지 삭제
    @DeleteMapping("/delete")
    public FaqResultDto faqDelete(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                  @RequestParam Long faqId){
        return faqService.deleteFaq(adminKey, faqId);
    }

    // 어드민 권한으로 faq 페이지 전체 조회
    @GetMapping("/list")
    public AllFaqDto getList(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey){
        return faqService.getAllFaq(adminKey);
    }

    // 어드민 권한으로 faq 페이지 공개 중단
    @PutMapping("/open-and-close")
    public FaqResultDto changeStatus(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                     @RequestBody StatusDto statusDto){
        return faqService.changeStatus(adminKey,statusDto);
    }

    @GetMapping("/members-faq-list")
    public AllFaqDto getMembersList(){
        return faqService.getMembersFaq();
    }



}
