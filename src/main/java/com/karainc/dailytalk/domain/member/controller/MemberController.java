package com.karainc.dailytalk.domain.member.controller;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karainc.dailytalk.domain.admin.dto.request.ChangePasswordDto;
import com.karainc.dailytalk.domain.exceptionhandler.DataRedundancyHandler;
import com.karainc.dailytalk.domain.member.dto.request.*;
import com.karainc.dailytalk.domain.member.dto.response.*;
import com.karainc.dailytalk.domain.member.service.MemberService;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/test")
    public String test(){
        return LocalDateTime.now().toString();
    }
    @PostMapping("/join")
    public JoinResultDto join(@RequestBody MemberDto memberDto){
        return memberService.join(memberDto);
    }

    @PostMapping("/rehab-join")
    public JoinResultDto rehabJoin(@RequestBody RehabMemberDto rehabMemberDto){
        return memberService.rehabJoin(rehabMemberDto);
    }

    @GetMapping("/check-id")
    public ResultDto checkId(@RequestParam String memberId){
        return memberService.checkId(memberId);
    }

    @GetMapping("/check-email")
    public ResultDto checkEmail(@RequestParam String email){
        return memberService.checkEmail(email);
    }

    @GetMapping("/check-nickname")
    public ResultDto checkNickName(@RequestParam String nickName){
        return memberService.checkNickName(nickName);
    }

    @PutMapping("/change-password")
    public ResultDto rePassword(@RequestHeader (value = "X-MEMBER-TOKEN" ,required = false)String memberKey,
                                @RequestBody ChangePasswordDto changePasswordDto){
        return memberService.rePassword(memberKey,changePasswordDto);
    }

    @GetMapping("/check-phone")
    public ResultDto checkPhoneNumber(@RequestParam String phoneNumber){
        return memberService.checkPn(phoneNumber);
    }

    @GetMapping("/find-email-by-id")
    public ResultDto findId(@RequestParam String email){
        return memberService.findId(email);
    }

    @GetMapping("/find-by-phone")
    public ResultDto findByPhone(@RequestParam String phoneNumber){
        return memberService.findIdByPhone(phoneNumber);
    }
    @GetMapping("/login")
    public MemberKeyDto login(@RequestParam String memberId,
                              @RequestParam String password){
        return memberService.login(memberId,password);
    }



    // 일반 회원 정보 조회
    @GetMapping("/info")
    public MemberInfoDto getInfo(@RequestHeader (value = "X-MEMBER-TOKEN" ,required = false)String memberKey){
        return memberService.getInfo(memberKey);
    }


    // 재활사 회원 정보 조회
    @GetMapping("/rehab-info")
    public RehabInfoDto getRehabInfo(@RequestHeader (value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return memberService.getRehabInfo(memberKey);
    }


    // 일반 회원 정보 수정
    @PutMapping("/change-info")
    public ResultDto updateInfo(@RequestHeader (value = "X-MEMBER-TOKEN" ,required = false)String memberKey,
                                @RequestBody UpdateMemberDto updateMemberDto){
        return memberService.changeInfo(memberKey,updateMemberDto);
    }

    // 재활사 정보 수정
    @PutMapping("/change-rehab-info")
    public ResultDto updateRehabInfo(@RequestHeader (value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                     @RequestBody UpdateRehabDto updateRehabDto){
        return memberService.updateReInfo(memberKey,updateRehabDto);
    }




    // 탈퇴
    @DeleteMapping("/out")
    public ResultDto deleteMember(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return memberService.deleteMember(memberKey);
    }

    @DeleteMapping("/rehab-out")
    public ResultDto deleteRehabMember(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return memberService.deleteRehabMember(memberKey);
    }

    @PutMapping("/change-profile")
    public ResultDto changeProfile(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                   @RequestParam MultipartFile newImage){
        return memberService.changeProfile(memberKey,newImage);
    }


    // 단순 엑세스 토큰 가져오기
    @GetMapping("/kakao/callback")
    public KakoToKenDto getT(@RequestParam("code")String code){
        return memberService.getCode(code);
    }

    @PostMapping("/join/kakao")
    public ResultDto KakaoMemberJoin(@RequestBody KakakoMemberDto kakakoMemberDto){
        return memberService.KakaoMember(kakakoMemberDto);
    }

    @PostMapping("/join/kakao-rehab")
    public ResultDto KakaoRehabJoin(@RequestBody KakaoRehabMemberDto kakaoRehabMemberDto){
        return memberService.KakaoRehMember(kakaoRehabMemberDto);
    }

    @PostMapping("/login/kakao")
    public MemberKeyDto kakaoLogin(@RequestBody LoginTokenDto loginTokenDto){
        return memberService.kakaoLogin(loginTokenDto);
    }




}
