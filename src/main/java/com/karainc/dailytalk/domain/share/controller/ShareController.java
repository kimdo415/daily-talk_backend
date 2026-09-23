package com.karainc.dailytalk.domain.share.controller;


import com.karainc.dailytalk.domain.share.controller.dto.requset.ShareBehavDto;
import com.karainc.dailytalk.domain.share.controller.dto.requset.ShareLangDto;
import com.karainc.dailytalk.domain.share.controller.dto.response.GetBehavDto;
import com.karainc.dailytalk.domain.share.controller.dto.response.GetLangDto;
import com.karainc.dailytalk.domain.share.controller.dto.response.ShareMsgDto;
import com.karainc.dailytalk.domain.share.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/share")
@RequiredArgsConstructor
public class ShareController {
    private final ShareService shareService;

    @PostMapping("/lang")
    public ShareMsgDto forShareL(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                @RequestBody ShareLangDto shareLangDto){
        return shareService.shareLang(memberKey,shareLangDto);
    }

    @GetMapping("/lang/{shareCode}")
    public GetLangDto shareLang(@PathVariable("shareCode")String shareCode){
        return shareService.getShareLang(shareCode);
    }

    @DeleteMapping("/delete-lang")
    public ShareMsgDto deleteShareLang(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                       @RequestParam Long langResultId){
        return shareService.deleteShareLang(memberKey,langResultId);
    }

    @PostMapping("/behav")
    public ShareMsgDto forShareB(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                 @RequestBody ShareBehavDto shareBehavDto){
        return shareService.shareBehav(memberKey,shareBehavDto);
    }

    @GetMapping("/behav/{shareCode}")
    public GetBehavDto shareBe(@PathVariable("shareCode")String shareCode){
        return shareService.shareB(shareCode);
    }

    @DeleteMapping("/delete-behav")
    public ShareMsgDto deleteBehavCon(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                      @RequestParam Long behavResultId){
        return shareService.deleteShareBe(memberKey,behavResultId);
    }
}
