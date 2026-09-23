package com.karainc.dailytalk.domain.notice.controller;

import com.karainc.dailytalk.domain.notice.controller.dto.request.NoteDto;
import com.karainc.dailytalk.domain.notice.controller.dto.request.NoticeDto;
import com.karainc.dailytalk.domain.notice.controller.dto.request.OpenCloseDto;
import com.karainc.dailytalk.domain.notice.controller.dto.response.*;
import com.karainc.dailytalk.domain.notice.service.NoticeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;


    //초안 작성
    @PostMapping("/new")
    public NoteIdxDto newNotice(@RequestHeader(value = "X-ADMIN-TOKEN", required = false)String adminKey,
                                @RequestBody NoticeDto noticeDto){
        return noticeService.getIdx(adminKey,noticeDto);
    }

    //서브타이츨 내용 타입 정하기
    @PostMapping("/content")
    public NoteResulDto oneNote(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                                @RequestBody NoteDto noteDto){
        return noticeService.makeNotice(adminKey,noteDto);
    }

    //새소식 내용에 이미지 넣기
    @PostMapping("/set-image")
    public ImageResultDto setImage(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                                   @RequestParam(value = "noticeIdx")Long noticeIdx,
                                   @RequestParam(value = "image",required = false)MultipartFile image){
        return noticeService.setImg(adminKey,noticeIdx,image);
    }

    //새소식에 썸네일 이미지 넣기
    @PostMapping("/set-thumbnail")
    public NoteResulDto setTh(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                              @RequestParam(value = "noticeIdx")Long noticeIdx,
                              @RequestParam(value = "thumbnail")MultipartFile thumbnail){
        return noticeService.setThumb(adminKey,noticeIdx,thumbnail);
    }

    //새소식 삭제
    @DeleteMapping("/out")
    public NoteResulDto outNotice(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                                  @RequestParam(value = "noticeIdx",required = false)List<Long> noticeIdx){
        return noticeService.deleteNote(adminKey,noticeIdx);
    }

    //어드민단 새소식 리스트 조회
    @GetMapping("/list-admin")
    public NoteListDto getA(@RequestHeader(value = "X-ADMIN-TOKEN")String adminKey){
        return noticeService.getAminList(adminKey);
    }

    //어드민단 새소식 상세 조회
    @GetMapping("/info-admin")
    public NoteDetailDto infoA(@RequestHeader(value = "X-ADMIN-TOKEN")String adminKey,
                               @RequestParam(value = "noticeIdx") Long noticeIdx){
        return noticeService.getAdminNoteInfo(adminKey,noticeIdx);
    }

    //어드민단 새소식 출력 상태 변환
    @PutMapping("/open-close")
    public NoteResulDto oc(@RequestHeader(value = "X-ADMIN-TOKEN")String adminKey,
                           @RequestBody OpenCloseDto openCloseDto){
        return noticeService.openClose(adminKey,openCloseDto);
    }

    //회원 비회원 모두 새소식리스트 조회
    @GetMapping("/list-member")
    public MemberNoteDto listMember(){
        return noticeService.getListMember();
    }

    // 회원 비회원 모두 새소식 상세조회
    @GetMapping("/info-member")
    public NoteDetailDto getInfoNotice(@RequestParam(value = "noticeIdx",required = false)Long noticeIdx){
        return noticeService.getMemberNoteInfo(noticeIdx);
    }
}
