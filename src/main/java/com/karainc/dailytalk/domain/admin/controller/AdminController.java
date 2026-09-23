package com.karainc.dailytalk.domain.admin.controller;


import com.karainc.dailytalk.domain.admin.dto.request.*;
import com.karainc.dailytalk.domain.admin.dto.response.*;
import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    // 어드민 회원 가입
    @PostMapping("/join")
    public ResultJoinDto adminJoin(@RequestBody AdminDto adminDto){
        return adminService.joinAdmin(adminDto);
    }


    // 어드민 로그인
    @PostMapping("/login")
    public AdminKey adminLogin(@RequestBody AdminLoginDto adminLoginDto){
        return adminService.adminLogin(adminLoginDto);
    }

    // 어드민 권한으로 현재 재활사 가입 대기건수 조회
    @GetMapping("/alarm")
    public AdminResultDto getCount(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false) String adminKey){
        return adminService.getCountRehab(adminKey);
    }

    // 어드민 권한으로 재활사 전체 목록 조회
    @GetMapping("/rehab-all")
    public RehabAllDto getAllRehabList(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey){
        return adminService.getRehabList(adminKey);
    }

    //어드민 권한으로 재활사 정보 보기
    @GetMapping("/get-info")
    public InfoDataDto getInfo(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                               @RequestParam Long memberIdx){
        return adminService.getReInfo(adminKey,memberIdx);
    }


    //어드민 권한으로 재활사 가입승인
    @PutMapping("/permit-rehab")
    public AdminResultDto changeRehab(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                      @RequestBody PermitDto permitDto){
        return adminService.permitRehab(adminKey,permitDto);
    }


    // 어드민 권한으로 일반 회원 보기
    @GetMapping("/member-list")
    public ResultMemberListDto getMemberList(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey){
        return adminService.getMemberList(adminKey);
    }

    // 어드민 권한으로 일반 회원 상세조회
    @GetMapping("/member-info")
    public ResultMemberInfoDetailDto getMemberInfo(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                                   @RequestParam long memberIdx){
        return adminService.memberInfo(adminKey,memberIdx);
    }

    //어드민 권한으로 일반 회원 탈퇴
    @DeleteMapping("/out")
    public ResultDto out(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                         @RequestParam long memberIdx){
        return adminService.deleteUser(adminKey,memberIdx);
    }

    // 어드민 권한으로 재활사 회원 정보 강제수정
    @PutMapping("/update-rehab-info")
    public ResultDto updateRehabByAdmin(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                        @RequestParam(value = "memberIdx",required = false)Long memberIdx,
                                        @RequestParam(value = "birth",required = false)String birth,
                                        @RequestParam(value = "address",required = false)String address,
                                        @RequestParam(value = "guardian",required = false)String guardian,
                                        @RequestParam(value = "phone1",required = false)String phone1,
                                        @RequestParam(value = "phone2",required = false)String phone2,
                                        @RequestParam(value = "parentsSex",required = false)String parentsSex,
                                        @RequestParam(value = "parentsBirth",required = false)String parentsBirth,
                                        @RequestParam(value = "mkService",required = false)Boolean mkService,
                                        @RequestParam(value = "detailAddress",required = false)String detailAddress,
                                        @RequestParam(value = "activityArea",required = false)String activityArea,
                                        @RequestParam(value = "cert",required = false)List<String> cert,
                                        @RequestParam(value = "careerDay",required = false)List<String> careerDay,
                                        @RequestParam(value = "career",required = false)List<String> career,
                                        @RequestParam(value = "division",required = false)String division,
                                        @RequestParam(value = "intro",required = false)String intro,
                                        @RequestParam(value = "profile",required = false)MultipartFile profile,
                                        @RequestParam(value = "checkEnum",required = false)String checkEnum){

        return adminService.updateReByAdmin(adminKey,memberIdx,birth,address,guardian,phone1,phone2,parentsSex,
                parentsBirth,mkService,detailAddress,activityArea,cert,careerDay,career,division,intro,profile,checkEnum);
    }

    // 어드민 권한으로 일반 회원 정보 강제 수정
    @PutMapping("/update-member-info")
    public ResultDto updateMemberByAdmin(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                         @RequestParam(value = "memberIdx",required = false)Long memberIdx,
                                         @RequestParam(value = "sex",required = false)String sex,
                                         @RequestParam(value = "name",required = false)String name,
                                         @RequestParam(value = "morePn",required = false)String morePn,
                                         @RequestParam(value = "guardian",required = false)String guardian,
                                         @RequestParam(value = "address",required = false)String address,
                                         @RequestParam(value = "birth",required = false)String birth,
                                         @RequestParam(value = "phone1",required = false)String phone1,
                                         @RequestParam(value = "phone2",required = false)String phone2,
                                         @RequestParam(value = "parentsSex",required = false)String parentsSex,
                                         @RequestParam(value = "parentsBirth",required = false)String parentsBirth,
                                         @RequestParam(value = "memberShip",required = false)String memberShip,
                                         @RequestParam(value = "mkService",required = false)Boolean mkService,
                                         @RequestParam(value = "profile",required = false)MultipartFile profile){
        return adminService.updateMemberByAdmin(adminKey,memberIdx,sex,name,morePn,guardian,address,birth,phone1,
                phone2,parentsSex,parentsBirth,memberShip,mkService,profile);
    }


    @PutMapping("/change-profile")
    public ResultDto updateProfile(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                   @RequestParam MultipartFile newImage,
                                   @RequestParam Long memberIdx){
        return adminService.updateProfile(adminKey,memberIdx,newImage);
    }

    @PutMapping("/member-ben")
    public ResultDto MemberBen(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                               @RequestBody BenMemberDto benMemberDto){
        return adminService.ben(adminKey,benMemberDto);
    }





}
