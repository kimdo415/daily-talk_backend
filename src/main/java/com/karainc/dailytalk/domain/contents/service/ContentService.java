package com.karainc.dailytalk.domain.contents.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.animation.controller.dto.requset.MakeDto;
import com.karainc.dailytalk.domain.contents.controller.dto.data.*;
import com.karainc.dailytalk.domain.contents.controller.dto.request.ChangeDto;
import com.karainc.dailytalk.domain.contents.controller.dto.response.*;
import com.karainc.dailytalk.domain.contents.entity.CheckConMember;
import com.karainc.dailytalk.domain.contents.entity.ConsultMember;
import com.karainc.dailytalk.domain.contents.entity.Contents;
import com.karainc.dailytalk.domain.contents.entity.SubConMember;
import com.karainc.dailytalk.domain.contents.repository.CheckConMemberRepository;
import com.karainc.dailytalk.domain.contents.repository.ConsultMemberRepository;
import com.karainc.dailytalk.domain.contents.repository.ContentsRepository;
import com.karainc.dailytalk.domain.contents.repository.SubConMemberRepository;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.member.service.MemberService;
import com.karainc.dailytalk.domain.utils.s3.service.S3UploadService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContentService {

    private final ContentsRepository contentsRepository;
    private final AdminService adminService;
    private final S3UploadService s3UploadService;
    private final CheckConMemberRepository checkConMemberRepository;
    private final SubConMemberRepository subConMemberRepository;
    private final ConsultMemberRepository consultMemberRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    // 구독형 컨텐츠 생성
    public ContentsMakeDto createSubContents(String adminKey, String contentName, String intro,
                                             Integer price, MultipartFile image,
                                             String duringDate, Boolean viewStatus){

        log.info("상품 생성");

        adminService.checkAdmin(adminKey);


        if(contentName==null || intro==null || price==null || duringDate==null || viewStatus==null){
            throw new NoDataExceptionHandler("양식을 전부 작성해주세요");
        }
        String getImage;

        if(image==null || image.isEmpty()){
            getImage=null;
        }else if(image.getSize()>30000000){
            throw new DataNotMatchHandler("30MB 크기는 업로드 할 수 없습니다");
        }else{
            String fileExtension= image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date()) + fileExtension;
            getImage=s3UploadService.uploadImage(image,"contents/subscribe"+newFileName);
        }

        Contents contents = Contents.builder()
                .contentsName(contentName)
                .intro(intro)
                .price(price)
                .contentsImage(getImage)
                .contentType("01")
                .duringDate(duringDate)
                .viewStatus(viewStatus)
                .date(LocalDate.now().toString())
                .sailCount(0)
                .build();


        contentsRepository.save(contents);

        ContentsId contentsId = new ContentsId();
        contentsId.setContentsIdx(contents.getContentsIdx());

        ContentsMakeDto contentsMakeDto = new ContentsMakeDto();
        contentsMakeDto.setStatus("200");
        contentsMakeDto.setMessage("상품 생성 완료");
        contentsMakeDto.setData(contentsId);
        return contentsMakeDto;
    }

    // 검사및 상담 검사 유형 콘텐츠 생성
    public ContentsMakeDto createCheckContents(String adminKey, String contentName,String intro,
                                       Integer price,MultipartFile image,String type,
                                       Integer useCount,Boolean viewStatus){

        log.info("상품 생성");

        adminService.checkAdmin(adminKey);
        if(contentName==null || intro==null || price==null || viewStatus==null){
            throw new NoDataExceptionHandler("양식을 전부 작성해주세요");
        }

        if(type==null || type.isEmpty()){
            throw new NoDataExceptionHandler("컨텐츠 유형을 넣어주세요");
        }else if(!(type.equals("02") || type.equals("03"))){
            throw new DataNotMatchHandler("컨텐츠 유형이 유효하지 않습니다");
        }

        String getImage;

        if(image==null || image.isEmpty()){
            getImage=null;
        }else if(image.getSize()>30*30*1024){
            throw new DataNotMatchHandler("30MB 크기는 업로드 할 수 없습니다");
        }else{
            String fileExtension= image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date()) + fileExtension;
            getImage=s3UploadService.uploadImage(image,"contents/check"+type+newFileName);
        }

        Contents contents = Contents.builder()
                .contentsName(contentName)
                .intro(intro)
                .price(price)
                .contentsImage(getImage)
                .contentType(type)
                .useCount(useCount)
                .viewStatus(viewStatus)
                .date(LocalDate.now().toString())
                .sailCount(0)
                .build();


        contentsRepository.save(contents);


        ContentsId contentsId = new ContentsId();
        contentsId.setContentsIdx(contents.getContentsIdx());

        ContentsMakeDto contentsMakeDto = new ContentsMakeDto();
        contentsMakeDto.setStatus("200");
        contentsMakeDto.setMessage("상품 생성 완료");
        contentsMakeDto.setData(contentsId);
        return contentsMakeDto;
    }




    // 구독형 컨텐츠 수정
    public ResultDto changeSubUpdate(String adminKey,Long contentsIdx,String contentName,String intro,
                                  Integer price,MultipartFile image,String duringDate){

        adminService.checkAdmin(adminKey);

        Contents contents = contentsRepository.findByContentsIdx(contentsIdx);
        if(contents==null){
            throw new NoDataExceptionHandler("삭제된 컨텐츠 입니다");
        }

        String cName= contents.getContentsName();
        String citro = contents.getIntro();
        Integer getPrice = contents.getPrice();
        String getImage = contents.getContentsImage();
        String date;

        if(contentName !=null){
            cName=contentName;
        }

        if(intro !=null){
            citro=intro;
        }

        if(price != null){
            getPrice = price;
        }

        if(duringDate==null || duringDate.isEmpty()){
            date=contents.getDuringDate();
        }else {
            date=duringDate;
        }

        if(image !=null){
            if(image.getSize()>30*30*1024){
                throw new DataNotMatchHandler("30MB 크기는 업로드 할 수 없습니다");
            }else{
                String fileExtension= image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String newFileName = dateFormat.format(new Date()) + fileExtension;
                getImage=s3UploadService.uploadImage(image,"contents/subscribe"+newFileName);
            }
        }

        contents.setContentsName(cName);
        contents.setIntro(citro);
        contents.setPrice(getPrice);
        contents.setContentsImage(getImage);
        contents.setDuringDate(date);
        contents.setDate(LocalDate.now().toString());
        contentsRepository.save(contents);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setData("컨텐츠 수정 완료(관리자 권한)");
        resultDto.setMessage("컨텐츠 수정 완료(관리자 권한)");

        return resultDto;
    }

    public ResultDto changeContentsStatus(String adminKey, ChangeDto changeDto){
        adminService.checkAdmin(adminKey);

        Contents contents = contentsRepository.findByContentsIdx(changeDto.getContentsIdx());
        if(contents == null){
            throw new NoDataExceptionHandler("존재하지 않는 컨텐츠 입니다");
        }

        String msg;
        Boolean nowStatus;
        if(contents.getViewStatus()==Boolean.TRUE){
            nowStatus=Boolean.FALSE;
            msg = "상품 내리기 완료";
        }else {
            nowStatus=Boolean.TRUE;
            msg = "상품 올리기 완료";
        }

        contents.setViewStatus(nowStatus);
        contentsRepository.save(contents);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage(msg);
        resultDto.setData(msg);

        return resultDto;
    }

    public ResultDto changeCheckUpdate(String adminKey,Long contentsIdx,String contentName,String intro,
                                     Integer price,String type,Integer useCount,MultipartFile image){

        adminService.checkAdmin(adminKey);

        Contents contents = contentsRepository.findByContentsIdx(contentsIdx);
        if(contents==null){
            throw new NoDataExceptionHandler("삭제된 컨텐츠 입니다");
        }


        String cName= contents.getContentsName();
        String citro = contents.getIntro();
        Integer getPrice = contents.getPrice();
        String getImage = contents.getContentsImage();
        String getType = contents.getContentType();
        Integer getUse;

        if(contentName !=null){
            cName=contentName;
        }

        if(intro !=null){
            citro=intro;
        }

        if(price != null){
            getPrice = price;
        }

        if(type !=null){
            if(!(type.equals("02") || type.equals("03"))){
                throw new DataNotMatchHandler("컨텐츠 유형이 유효하지 않습니다");
            }
            getType =type;
        }

        if(useCount ==null){
            getUse =contents.getUseCount();
        }else {
            getUse = useCount;
        }


        if(image !=null){
            if(image.getSize()>30*30*1024){
                throw new DataNotMatchHandler("30MB 크기는 업로드 할 수 없습니다");
            }else{
                String fileExtension= image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String newFileName = dateFormat.format(new Date()) + fileExtension;
                getImage=s3UploadService.uploadImage(image,"contents/"+getType+newFileName);
            }
        }



        contents.setContentsName(cName);
        contents.setIntro(citro);
        contents.setPrice(getPrice);
        contents.setContentsImage(getImage);
        contents.setContentType(getType);
        contents.setUseCount(getUse);
        contents.setDate(LocalDate.now().toString());
        contentsRepository.save(contents);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setData("컨텐츠 수정 완료(관리자 권한)");
        resultDto.setMessage("컨텐츠 수정 완료(관리자 권한)");

        return resultDto;
    }

    public ContentsListDto getAll(String adminKey){
        adminService.checkAdmin(adminKey);

        List<Contents> contents = contentsRepository.findAll();
        List<ContentList> contentsInfoList = new ArrayList<>();
        int i =0;
        for(Contents getContent : contents){
            ContentList contentsInfo = new ContentList();
            contentsInfo.setContentsIdx(getContent.getContentsIdx());
            contentsInfo.setContentsName(getContent.getContentsName());
            contentsInfo.setPrice(getContent.getPrice());
            contentsInfo.setViewStatus(getContent.getViewStatus());
            contentsInfo.setDate(getContent.getDate());
            contentsInfo.setSailCount(getContent.getSailCount());
            contentsInfoList.add(contentsInfo);
            i++;
        }
        if(i==0){
            throw new NoDataExceptionHandler("등록된 상품이 없습니다 상품을 먼저 등록해주세요");
        }
        ContentsListDto contentsListDto = new ContentsListDto();
        contentsListDto.setStatus("200");
        contentsListDto.setMessage("상품 목록 조회 (관리자 권한)");
        contentsListDto.setData(contentsInfoList);
        return  contentsListDto;
    }

    public AdminConInfo getAInfo(String adminKey,Long contentsIdx){
        adminService.checkAdmin(adminKey);

        Contents contents = contentsRepository.findByContentsIdx(contentsIdx);
        if(contents==null){
            throw new NoDataExceptionHandler("해당 상품이 존재하지 않습니다");
        }

        ContentInfo contentInfo = new ContentInfo();
        contentInfo.setContentsIdx(contents.getContentsIdx());
        contentInfo.setContentsName(contents.getContentsName());
        contentInfo.setIntro(contents.getIntro());
        contentInfo.setPrice(contents.getPrice());
        contentInfo.setContentsImage(contents.getContentsImage());
        contentInfo.setContentType(contents.getContentType());
        contentInfo.setDuringDate(contents.getDuringDate());
        contentInfo.setUseCount(contents.getUseCount());
        contentInfo.setViewStatus(contents.getViewStatus());
        contentInfo.setDate(contents.getDate());
        contentInfo.setSailCount(contents.getSailCount());

        AdminConInfo adminConInfo = new AdminConInfo();
        adminConInfo.setStatus("200");
        adminConInfo.setMessage("상품 상세정보 : 관리자 권한");
        adminConInfo.setData(contentInfo);

        return adminConInfo;
    }



    public ResultDto deleteContents(String adminKey, Long contentsIdx){
        adminService.checkAdmin(adminKey);
        Contents contents = contentsRepository.findByContentsIdx(contentsIdx);

        if(contents == null){
            throw new NoDataExceptionHandler("없는 컨텐츠 입니다");
        }

        contentsRepository.delete(contents);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setData("컨텐츠 삭제(관리자 권한)");
        resultDto.setMessage("컨텐츠 삭제(관리자 권한)");

        return resultDto;
    }

    public MemberViewListDto getMemberView(){
        List<Contents> contentList = contentsRepository.findByViewStatus(Boolean.TRUE);

        int k= contentList.size();

        if(k==0){
            throw new DataNotMatchHandler("상품 중비중입니다");
        }

        List<MemberViewList> memberViewLists = new ArrayList<>();

        for(Contents contents : contentList){
            MemberViewList memberViewList = new MemberViewList();
            memberViewList.setContentsIdx(contents.getContentsIdx());
            memberViewList.setContentsName(contents.getContentsName());
            memberViewList.setContentsImage(contents.getContentsImage());
            memberViewList.setIntro(contents.getIntro());
            memberViewList.setPrice(contents.getPrice());

            memberViewLists.add(memberViewList);
        }

        MemberViewListDto memberViewListDto = new MemberViewListDto();
        memberViewListDto.setStatus("200");
        memberViewListDto.setMessage("상품 조회");
        memberViewListDto.setData(memberViewLists);

        return memberViewListDto;
    }

    public MemberCtInfoDto getMInfoView(Long contentsIdx){
        Contents contents = contentsRepository.findByContentsIdx(contentsIdx);

        if(contents==null){
            throw new NoDataExceptionHandler("존재하지 않는 상품입니다");
        }else if(contents.getViewStatus()==Boolean.FALSE){
            throw new DataNotMatchHandler("해당상품은 판매 중지되었습니다");
        }

        MemberCtInfoDto memberCtInfoDto = new MemberCtInfoDto();

        MemberConInfo memberConInfo = new MemberConInfo();
        memberConInfo.setContentsIdx(contents.getContentsIdx());
        memberConInfo.setContentsName(contents.getContentsName());
        memberConInfo.setIntro(contents.getIntro());
        memberConInfo.setPrice(contents.getPrice());
        memberConInfo.setContentsImage(contents.getContentsImage());
        memberConInfo.setContentType(contents.getContentType());
        memberConInfo.setDuringDate(contents.getDuringDate());
        memberConInfo.setUseCount(contents.getUseCount());

        memberCtInfoDto.setStatus("200");
        memberCtInfoDto.setMessage("상품 상세 정보");
        memberCtInfoDto.setData(memberConInfo);

        return memberCtInfoDto;
    }

    // 구독형 컨텐츠 멤버 권한 확인 (01)
    public void findSubMember(Member member){
        SubConMember subConMember = subConMemberRepository.findByMember(member);

        if(subConMember==null){
            throw new NoDataExceptionHandler("결제후 사용 가능합니다");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        LocalDate memberEndDate = LocalDate.parse(subConMember.getEndDate(),formatter);

        if(now.isAfter(memberEndDate)){
            throw new DataNotMatchHandler("구독기간이 종료 되었습니다");
        }

    }

    // 상담형 컨텐츠 조회 02
    public void findConsultMember(Member member){
       ConsultMember consultMember=consultMemberRepository.findByMember(member);

       if(consultMember==null){
           throw new NoDataExceptionHandler("결제후 사용 가능합니다");
       }else if(consultMember.getUseCount()<0){
           throw new NoDataExceptionHandler("현제 남은 상담횟수는 0 입니다");
       }
    }

    // 상담형 횟수 차감 02
    public void minusCountConsult(Member member){
        ConsultMember consultMember=consultMemberRepository.findByMember(member);
        consultMember.setUseCount(consultMember.getUseCount()-1);

        consultMemberRepository.save(consultMember);
    }

    // 진단검사형 컨텐츠 조회 03
    public void findCheckMember(Member member){
        CheckConMember checkConMember = checkConMemberRepository.findByMember(member);
        if(checkConMember==null){
            throw new NoDataExceptionHandler("결제후 사용 가능합니다");
        } else if(checkConMember.getUseCount()<0){
            throw new NoDataExceptionHandler("현제 남은 검사 횟수는 0 입니다");
        }
    }

    // 진단검사형 컨텐츠 횟수 차감 03
    public void minusCountCheck(Member member){
        CheckConMember checkConMember = checkConMemberRepository.findByMember(member);
        checkConMember.setUseCount(checkConMember.getUseCount()-1);
        checkConMemberRepository.save(checkConMember);
    }


    public ContentsInfoDto myContents(String memberKey){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);

        SubConMember subConMember = subConMemberRepository.findByMember(member);
        SubConInfo subConInfo = new SubConInfo();

        if(subConMember == null){
            subConInfo.setType(null);
            subConInfo.setName(null);
            subConInfo.setTitle(null);
            subConInfo.setStartDate(null);
            subConInfo.setEndDate(null);

        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(subConMember.getEndDate(),formatter).minusDays(Integer.parseInt(subConMember.getContents().getDuringDate()));
            subConInfo.setType(subConMember.getContents().getContentType());
            subConInfo.setName(subConMember.getContents().getContentsName());
            subConInfo.setTitle(subConMember.getContents().getIntro());
            subConInfo.setStartDate(startDate.toString());
            subConInfo.setEndDate(subConMember.getEndDate());

        }

        CheckConInfo checkConInfo = new CheckConInfo();
        CheckConMember checkConMember =checkConMemberRepository.findByMember(member);
        if(checkConMember==null){
            checkConInfo.setType(null);
            checkConInfo.setTitle(null);
            checkConInfo.setName(null);
            checkConInfo.setUseCount(null);
        }else{
            checkConInfo.setType(checkConMember.getContents().getContentType());
            checkConInfo.setTitle(checkConMember.getContents().getIntro());
            checkConInfo.setName(checkConMember.getContents().getContentsName());
            checkConInfo.setUseCount(checkConMember.getUseCount());
        }

        ConsultInfo consultInfo = new ConsultInfo();

        ConsultMember consultMember = consultMemberRepository.findByMember(member);
        if(consultMember==null){
            consultInfo.setType(null);
            consultInfo.setName(null);
            consultInfo.setTitle(null);
            consultInfo.setUseCount(null);
        }else{
            consultInfo.setType(consultMember.getContents().getContentType());
            consultInfo.setName(consultMember.getContents().getContentsName());
            consultInfo.setTitle(consultMember.getContents().getIntro());
            consultInfo.setUseCount(consultMember.getUseCount());
        }

        TotalContents totalContents = new TotalContents();
        totalContents.setCheckData(checkConInfo);
        totalContents.setSubData(subConInfo);
        totalContents.setConsultData(consultInfo);

        ContentsInfoDto contentsInfoDto = new ContentsInfoDto();
        contentsInfoDto.setStatus("200");
        contentsInfoDto.setMessage("이용권 정보");
        contentsInfoDto.setData(totalContents);

        return contentsInfoDto;
    }






}
