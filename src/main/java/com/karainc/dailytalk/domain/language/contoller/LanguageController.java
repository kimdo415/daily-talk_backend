package com.karainc.dailytalk.domain.language.contoller;




import com.karainc.dailytalk.domain.language.contoller.dto.requset.CallLangDto;
import com.karainc.dailytalk.domain.language.contoller.dto.requset.DeleteLangDto;
import com.karainc.dailytalk.domain.language.contoller.dto.requset.MyLangListDto;
import com.karainc.dailytalk.domain.language.contoller.dto.requset.UpdateLangDto;
import com.karainc.dailytalk.domain.language.contoller.dto.response.AllMemberDto;
import com.karainc.dailytalk.domain.language.contoller.dto.response.LangListDto;
import com.karainc.dailytalk.domain.language.contoller.dto.response.LangMsgDto;
import com.karainc.dailytalk.domain.language.contoller.dto.response.MyLangDto;
import com.karainc.dailytalk.domain.language.service.LanguageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/language")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping("/create-lang")
    public LangMsgDto clang(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return languageService.create(adminKey);
    }

    @PostMapping("/plus-lang")
    public LangMsgDto plusLang(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                               @RequestBody CallLangDto callLangDto){
        return languageService.plus(adminKey,callLangDto);
    }

    @PutMapping("/change-lang")
    public LangMsgDto updateLang(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                 @RequestBody UpdateLangDto updateLangDto){
        return languageService.update(adminKey,updateLangDto);
    }

    @PutMapping("/delete-lang")
    public LangMsgDto part(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                           @RequestBody DeleteLangDto deleteLangDto){
        return languageService.partDel(adminKey,deleteLangDto);
    }

    @GetMapping("/admin-view")
    public LangListDto forAdmin(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return languageService.ForAdmin(adminKey);
    }

    @GetMapping("/result-list")
    public AllMemberDto getAllDash(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return languageService.getMembersData(adminKey);
    }

    @GetMapping
    public LangListDto forMember(@RequestHeader(value = "X-MEMBER-TOKEN" ,required = false)String memberKey){
        return languageService.ForMember(memberKey);
    }

    @PostMapping("/submit")
    public LangMsgDto submit(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                             @RequestBody MyLangListDto myLangListDto){
        return languageService.submit(memberKey,myLangListDto);
    }

    @GetMapping("/my-result")
    public MyLangDto getDash(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return languageService.getMyDashBoard(memberKey);
    }


    @GetMapping("/test-view")
    public LangListDto getTest(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return languageService.testView(memberKey);
    }

    @PostMapping("/test")
    public LangMsgDto testSubmit(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                 @RequestBody MyLangListDto myLangListDto){
        return languageService.testSub(memberKey,myLangListDto);
    }


}
