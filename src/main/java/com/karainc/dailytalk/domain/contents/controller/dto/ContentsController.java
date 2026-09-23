package com.karainc.dailytalk.domain.contents.controller.dto;


import com.karainc.dailytalk.domain.contents.controller.dto.request.ChangeDto;
import com.karainc.dailytalk.domain.contents.controller.dto.response.*;
import com.karainc.dailytalk.domain.contents.service.ContentService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentsController {

    private final ContentService contentService;


    // 구독형 컨텐츠 생성
    @PostMapping("/create-subscribe")
    public ContentsMakeDto makeSub(@RequestHeader(value = "X-ADMIN-TOKEN", required = false) String adminKey,
                                   @RequestParam(value = "contentsName",required = false)String contentName,
                                   @RequestParam(value = "intro",required = false)String intro,
                                   @RequestParam(value = "price",required = false)Integer price,
                                   @RequestParam(value = "contentsImage",required = false)MultipartFile image,
                                   @RequestParam(value = "duringDate",required = false)String duringDate,
                                   @RequestParam(value = "viewStatus",required = false)Boolean viewStatus){

        return contentService.createSubContents(adminKey,contentName,intro,price,image,duringDate,viewStatus);
    }

    // 진단검사 컨텐츠 생성
    @PostMapping("/create-check")
    public ContentsMakeDto makeCheck(@RequestHeader(value = "X-ADMIN-TOKEN", required = false) String adminKey,
                               @RequestParam(value = "contentsName",required = false)String contentName,
                               @RequestParam(value = "intro",required = false)String intro,
                               @RequestParam(value = "price",required = false)Integer price,
                               @RequestParam(value = "contentsImage",required = false)MultipartFile image,
                               @RequestParam(value = "contentType",required = false)String type,
                               @RequestParam(value = "useCount",required = false)Integer useCount,
                               @RequestParam(value = "viewStatus",required = false)Boolean viewStatus){

        return contentService.createCheckContents(adminKey,contentName,intro,price,image,type,useCount,viewStatus);
    }

    // 구독형 컨텐츠 수정
    @PutMapping("/update-subscribe")
    public ResultDto subUpdate(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                               @RequestParam(value = "contentsIdx") Long contentsIdx,
                               @RequestParam(value = "contentsName",required = false)String contentName,
                               @RequestParam(value = "intro",required = false)String intro,
                               @RequestParam(value = "price",required = false)Integer price,
                               @RequestParam(value = "duringDate",required = false)String duringDate,
                               @RequestParam(value = "contentsImage",required = false)MultipartFile image){

        return contentService.changeSubUpdate(adminKey,contentsIdx,contentName,intro,price,image,duringDate);

    }

    // 진단검사 컨텐츠 수정
    @PutMapping("/update-check")
    public ResultDto checkUpdate(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                 @RequestParam(value = "contentsIdx") Long contentsIdx,
                                 @RequestParam(value = "contentsName",required = false)String contentName,
                                 @RequestParam(value = "intro",required = false)String intro,
                                 @RequestParam(value = "price",required = false)Integer price,
                                 @RequestParam(value = "contentType",required = false)String type,
                                 @RequestParam(value = "contentsImage",required = false)MultipartFile image,
                                 @RequestParam(value = "useCount",required = false)Integer useCount){

        return contentService.changeCheckUpdate(adminKey,contentsIdx,contentName,intro,price,type,useCount,image);

    }

    @PutMapping("/open-and-close")
    public ResultDto changeStatus(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                  @RequestBody ChangeDto changeDto){
        return contentService.changeContentsStatus(adminKey,changeDto);
    }


    // 컨텐츠 전부 조회
    @GetMapping("/list/admin-view")
    public ContentsListDto getList(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey){
        return contentService.getAll(adminKey);
    }

    // 컨텐츠 상세조회
    @GetMapping("/list/admin-view/{contentsIdx}")
    public AdminConInfo getInfo(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                @PathVariable("contentsIdx")Long contentsIdx){
        return contentService.getAInfo(adminKey,contentsIdx);
    }


    // 컨텐츠 삭제
    @DeleteMapping("/delete")
    public ResultDto outCont(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                             @RequestParam(value = "contentsIdx") Long contentsIdx){
        return contentService.deleteContents(adminKey,contentsIdx);
    }

    //컨텐츠 전부 조회(회원단)
    @GetMapping("/list")
    public MemberViewListDto getMList(){
        return contentService.getMemberView();
    }

    //컨텐츠 상세 조회(회원단)
    @GetMapping("/list/{contentsIdx}")
    public MemberCtInfoDto getMInfo(@PathVariable("contentsIdx")Long contentsIdx){
        return contentService.getMInfoView(contentsIdx);
    }

    @GetMapping("/my-contents")
    public ContentsInfoDto getMyCon(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return contentService.myContents(memberKey);
    }



}
