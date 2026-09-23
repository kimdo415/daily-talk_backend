package com.karainc.dailytalk.domain.notice.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.notice.controller.dto.data.MemberNote;
import com.karainc.dailytalk.domain.notice.controller.dto.data.NoteDetail;
import com.karainc.dailytalk.domain.notice.controller.dto.data.NoticeListDto;
import com.karainc.dailytalk.domain.notice.controller.dto.request.NoteDto;
import com.karainc.dailytalk.domain.notice.controller.dto.request.NoticeDto;
import com.karainc.dailytalk.domain.notice.controller.dto.request.OpenCloseDto;
import com.karainc.dailytalk.domain.notice.controller.dto.response.*;
import com.karainc.dailytalk.domain.notice.entity.Notice;
import com.karainc.dailytalk.domain.notice.repository.NoticeRepository;
import com.karainc.dailytalk.domain.utils.s3.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class NoticeService {
    private final AdminService adminService;
    private final NoticeRepository noticeRepository;
    private final S3UploadService s3UploadService;


    // 초안 작성
    public NoteIdxDto getIdx(String adminKey , NoticeDto noticeDto){
        adminService.checkAdmin(adminKey);
        if(noticeDto.getNoticeTitle()==null || noticeDto.getNoticeTitle().isEmpty()){
            throw new NoDataExceptionHandler("제목을 먼저 작성해주세요");
        }

        Notice notice = Notice.builder()
                .noticeTitle(noticeDto.getNoticeTitle())
                .content(null)
                .date(null)
                .thumbnail(null)
                .images(null)
                .type(null)
                .view(Boolean.TRUE)
                .build();

        long data = noticeRepository.save(notice).getNoticeIdx();

        log.info("어드민 권한 : 새소식 초안 작성");

        NoteIdxDto noteIdxDto =new NoteIdxDto();
        noteIdxDto.setStatus("200");
        noteIdxDto.setMessage("초안 작성 완료");
        noteIdxDto.setData(data);
        return noteIdxDto;
    }


    // 서브타이틀 , 내용, 타입 정하기
    public NoteResulDto makeNotice(String adminKey , NoteDto noteDto){
        adminService.checkAdmin(adminKey);
        Notice notice = noticeRepository.findByNoticeIdx(noteDto.getNoticeIdx());
        if(notice == null){
            throw new NoDataExceptionHandler("없는 새소식 입니다");
        }


        String title;
        String type;
        String content;

        if(noteDto.getContent()==null || noteDto.getContent().isEmpty()){
            log.info("내용 수정 없음");
            content= notice.getContent();
        }else {
            log.info("내용 수정");
            content=noteDto.getContent();
        }

        if(noteDto.getType()==null || noteDto.getType().isEmpty()){
            log.info("타입 변환 없음");
            type= notice.getType();
        }else {
            log.info("타입 변환");
            type= noteDto.getType();
        }

        if(noteDto.getNoticeTitle()==null || noteDto.getNoticeTitle().isEmpty()){
            log.info("제목 수정 없음");
            title= notice.getNoticeTitle();
        }else {
            log.info("제목수정");
            title= noteDto.getNoticeTitle();
        }

        notice.setNoticeTitle(title);
        notice.setContent(content);
        notice.setType(type);
        notice.setDate(LocalDate.now().toString());
        noticeRepository.save(notice);


        log.info("어드민 권한 : 새소식 작성");
        log.info("새소식 위치 : " + String.valueOf(notice.getNoticeIdx()));

        NoteResulDto noteResulDto = new NoteResulDto();
        noteResulDto.setStatus("200");
        noteResulDto.setMessage("새소식 작성완료(관리자 권한)");
        noteResulDto.setData("새소식 작성완료(관리자 권한)");
        return noteResulDto;
    }

    //새소식 내용에 이미지 넣기
    public ImageResultDto setImg(String adminKey,
                                 Long noticeIdx,
                                 MultipartFile image){
        adminService.checkAdmin(adminKey);
        Notice notice = noticeRepository.findByNoticeIdx(noticeIdx);
        if(notice==null){
            throw new NoDataExceptionHandler("없는 새소식 입니다");
        }

        String getImage;
        if(image==null || image.isEmpty()){
            log.info("이미지 온거 없음");
            getImage=null;
        }else if(image.getSize()>30000000) {
            throw new NoDataExceptionHandler("용량 초과 : 30MB 미만의 이미지만 업로드 가능합니다");
        }else {
            String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
            // 오늘 날짜를 포함한 새로운 파일 이름 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = "thumb"+dateFormat.format(new Date()) + fileExtension;
            getImage=s3UploadService.uploadImage(image,"notice/"+String.valueOf(notice.getNoticeIdx())+"/"+newFileName);
        }
        log.info("이미지 생성 결과");
        log.info(getImage);
        log.info("관리자 권한 : 새소식 본문 이미지 생성");
        log.info("새소식 위치 : " + String.valueOf(notice.getNoticeIdx()));

        ImageResultDto imageResultDto = new ImageResultDto();
        imageResultDto.setStatus("200");
        imageResultDto.setMessage("새소식 본문 이미지 생성(관리자 권한)");
        imageResultDto.setData(getImage);
        return imageResultDto;
    }

    //새소식 썸네일 이미지 넣기
    public NoteResulDto setThumb(String adminKey,
                                 Long noticeIdx,
                                 MultipartFile thumbnail){
        adminService.checkAdmin(adminKey);
        if(noticeIdx==null){
            throw new NoDataExceptionHandler("새소식 참조값 없음");
        }
        Notice notice = noticeRepository.findByNoticeIdx(noticeIdx);
        if(notice == null){
            throw new NoDataExceptionHandler("없는 새소식 입니다");
        }

        String getThImg;
        if(thumbnail==null || thumbnail.isEmpty()){
            log.info("이미지 온거 없음");
            getThImg=null;
        }else if(thumbnail.getSize()>30000000) {
            throw new NoDataExceptionHandler("용량 초과 : 30MB 미만의 이미지만 업로드 가능합니다");
        }else {
            String fileExtension = thumbnail.getOriginalFilename().substring(thumbnail.getOriginalFilename().lastIndexOf("."));
            // 오늘 날짜를 포함한 새로운 파일 이름 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = "thumb"+dateFormat.format(new Date()) + fileExtension;
            getThImg=s3UploadService.uploadImage(thumbnail,"notice/"+String.valueOf(notice.getNoticeIdx())+"/"+newFileName);
        }

        notice.setThumbnail(getThImg);
        noticeRepository.save(notice);

        log.info("관리자 권한 : 새소식 썸네일 이미지 생성");
        log.info("새소식 위치 : " + String.valueOf(notice.getNoticeIdx()));

        NoteResulDto noteResulDto = new NoteResulDto();
        noteResulDto.setStatus("200");
        noteResulDto.setMessage("새소식 썸네일 이미지 생성(관리자 권한)");
        noteResulDto.setData(getThImg);
        return noteResulDto;
    }

    //새소식 삭제
    public NoteResulDto deleteNote(String adminKey,List<Long> noticeIdx){
        adminService.checkAdmin(adminKey);

        if(noticeIdx==null || noticeIdx.isEmpty()){
            throw new NoDataExceptionHandler("삭제할 새소식을 골라주세요");
        }
        for(int i=0; i<noticeIdx.size(); i++){
            Notice notice  = noticeRepository.findByNoticeIdx(noticeIdx.get(i));
            if(notice!=null){
                log.info("관리자 권한 : 새소식 삭제");
                log.info("새소식 위치 : " + String.valueOf(notice.getNoticeIdx()));

                s3UploadService.deleteFolder("notice/"+String.valueOf(notice.getNoticeIdx()));
                noticeRepository.delete(notice);
            }
        }

        NoteResulDto noteResulDto = new NoteResulDto();
        noteResulDto.setStatus("200");
        noteResulDto.setMessage("새소식 삭제 (관리자 권한)");
        noteResulDto.setData("새소식 삭제 (관리자 권한)");
        return noteResulDto;
    }


    // 어드민단 새소식 리스트 조회
    public NoteListDto getAminList(String adminKey){
        adminService.checkAdmin(adminKey);
        List<Notice> notices= noticeRepository.findAll();
        if(notices.size()==0){
            throw new NoDataExceptionHandler("등록된 새소식이 없습니다");
        }

        List<NoticeListDto> noticeListDtos = new ArrayList<>();
        for(Notice notice : notices){
            NoticeListDto noteListDto = new NoticeListDto();
            noteListDto.setNoticeIdx(notice.getNoticeIdx());
            noteListDto.setNoticeTitle(notice.getNoticeTitle());
            noteListDto.setThumbNail(notice.getThumbnail());
            noteListDto.setType(notice.getType());
            noteListDto.setView(notice.isView());
            noteListDto.setDate(notice.getDate());
            noticeListDtos.add(noteListDto);
        }
        log.info("관리자 권한 : 새소식 목록 조회");

        NoteListDto noteResulDto = new NoteListDto();
        noteResulDto.setStatus("200");
        noteResulDto.setMessage("새소식 목록 조회(관리자 권한)");
        noteResulDto.setData(noticeListDtos);

        return noteResulDto;
    }

    // 어드민단 새소식 상세조회
    public NoteDetailDto getAdminNoteInfo(String adminKey, Long noticeIdx){
        adminService.checkAdmin(adminKey);
        if(noticeIdx==null){
            throw new NoDataExceptionHandler("참조값 없음");
        }
        Notice notice  = noticeRepository.findByNoticeIdx(noticeIdx);
        if(notice==null){
            throw new NoDataExceptionHandler("없는 새소식 입니다");
        }
        NoteDetail noteDetail = new NoteDetail();
        noteDetail.setNoticeIdx(notice.getNoticeIdx());
        noteDetail.setNoticeTitle(notice.getNoticeTitle());
        noteDetail.setThumbnail(notice.getThumbnail());
        noteDetail.setContent(notice.getContent());
        noteDetail.setType(notice.getType());
        noteDetail.setDate(notice.getDate());

        log.info("관리자 권한 : 새소식 상세 조회");
        log.info("새소식 위치 : " +String.valueOf(notice.getNoticeIdx()));

        NoteDetailDto noteDetailDto = new NoteDetailDto();
        noteDetailDto.setStatus("200");
        noteDetailDto.setMessage("새소식 상세 조회(관리자 권한)");
        noteDetailDto.setData(noteDetail);
        return noteDetailDto;
    }


    public NoteResulDto openClose(String adminKey, OpenCloseDto openCloseDto){
        adminService.checkAdmin(adminKey);
        Notice notice = noticeRepository.findByNoticeIdx(openCloseDto.getNoticeIdx());
        if(notice==null){
            throw new NoDataExceptionHandler("없는 새소식 입니다");
        }

        boolean st;
        String msg;

        if(notice.isView()==Boolean.FALSE){
            st=Boolean.TRUE;
            msg="새소식 공개 완료(관리자 권한)";
        }else {
            st=Boolean.FALSE;
            msg="새소식 공개 중지(관리자 권한)";
        }

        notice.setView(st);
        noticeRepository.save(notice);

        log.info("어드민 권한 : 새소식 공개 여부");
        log.info(msg);
        log.info("새소식 위치 : "+String.valueOf(notice.getNoticeIdx()));

        NoteResulDto noteResulDto = new NoteResulDto();
        noteResulDto.setStatus("200");
        noteResulDto.setMessage(msg);
        noteResulDto.setData(msg);
        return noteResulDto;
    }

    public MemberNoteDto getListMember(){
        List<Notice> notices = noticeRepository.findByView(true);
        int i =0;
        List<MemberNote> memberNoteList = new ArrayList<>();
        for(Notice notice: notices){
            MemberNote memberNote = new MemberNote();
            memberNote.setNoticeIdx(notice.getNoticeIdx());
            memberNote.setNoticeTitle(notice.getNoticeTitle());
            memberNote.setThumbNail(notice.getThumbnail());
            memberNote.setContent(notice.getContent());
            memberNote.setType(notice.getType());
            memberNote.setDate(notice.getDate());
            i = i+1;
            memberNoteList.add(memberNote);
        }
        if(i==0){
            throw new NoDataExceptionHandler("작성된 게시글이 없습니다");
        }

        MemberNoteDto memberNoteDto = new MemberNoteDto();
        memberNoteDto.setStatus("200");
        memberNoteDto.setMessage("새소식 목록");
        memberNoteDto.setData(memberNoteList);
        return memberNoteDto;
    }

    public NoteDetailDto getMemberNoteInfo(Long noticeIdx){
        if(noticeIdx==null){
            throw new NoDataExceptionHandler("참조값 없음");
        }
        Notice notice  = noticeRepository.findByNoticeIdx(noticeIdx);
        if(notice==null){
            throw new NoDataExceptionHandler("없는 새소식 입니다");
        }else if(notice.isView()==Boolean.FALSE){
            throw new DataNotMatchHandler("접근 불가능한 게시글 입니다");
        }
        NoteDetail noteDetail = new NoteDetail();
        noteDetail.setNoticeIdx(notice.getNoticeIdx());
        noteDetail.setNoticeTitle(notice.getNoticeTitle());
        noteDetail.setThumbnail(notice.getThumbnail());
        noteDetail.setContent(notice.getContent());
        noteDetail.setType(notice.getType());
        noteDetail.setDate(notice.getDate());

        log.info("새소식 상세 조회 (회원쪽)");
        log.info("새소식 위치 : " +String.valueOf(notice.getNoticeIdx()));

        NoteDetailDto noteDetailDto = new NoteDetailDto();
        noteDetailDto.setStatus("200");
        noteDetailDto.setMessage("새소식 상세 조회(관리자 권한)");
        noteDetailDto.setData(noteDetail);
        return noteDetailDto;
    }




}
