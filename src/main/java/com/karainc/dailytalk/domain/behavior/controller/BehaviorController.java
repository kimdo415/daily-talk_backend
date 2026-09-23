package com.karainc.dailytalk.domain.behavior.controller;


import com.karainc.dailytalk.domain.behavior.controller.dto.requset.*;
import com.karainc.dailytalk.domain.behavior.controller.dto.response.AdminBeDto;
import com.karainc.dailytalk.domain.behavior.controller.dto.response.BeMsgDto;
import com.karainc.dailytalk.domain.behavior.controller.dto.response.UserBeDto;
import com.karainc.dailytalk.domain.behavior.service.BehaviorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/behavior")
@RequiredArgsConstructor
public class BehaviorController {
    private final BehaviorService behaviorService;


    @PostMapping("/make-behavior")
    public BeMsgDto make(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return behaviorService.create(adminKey);
    }

    @PostMapping("/plus-behavior")
    public BeMsgDto plus(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                         @RequestBody PlusBeDto plusBeDto){
        return behaviorService.plusBe(adminKey,plusBeDto);
    }

    @PutMapping("/change-behavior")
    public BeMsgDto changeB(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                            @RequestBody UpdateBeDto updateBeDto){
        return behaviorService.updateBe(adminKey,updateBeDto);
    }

    @PutMapping("/delete-behavior")
    public BeMsgDto delB(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                         @RequestBody DeleteBeDto deleteBeDto){
        return behaviorService.deleteBe(adminKey,deleteBeDto);
    }

    @GetMapping("/admin-view")
    public AdminBeDto getAd(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return behaviorService.forAdmin(adminKey);
    }

    @GetMapping("/result-list")
    public AllMemberDto getAllData(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey){
        return behaviorService.getAllDashB(adminKey);
    }
    @GetMapping
    public UserBeDto getM(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return behaviorService.forMember(memberKey);
    }



    @PostMapping("/submit")
    public BeMsgDto submit(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                           @RequestBody BehavReDto behavReDto){
        return behaviorService.review(memberKey,behavReDto);
    }


    @GetMapping("/my-result")
    public MyDataDto myDashB(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return behaviorService.getMyDashB(memberKey);
    }

    @GetMapping("/test-view")
    public UserBeDto getTest(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return behaviorService.forMemberTest(memberKey);
    }



    @PostMapping("/test")
    public BeMsgDto submitTest(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                           @RequestBody BehavReDto behavReDto){
        return behaviorService.reviewTest(memberKey,behavReDto);
    }

}
