package com.karainc.dailytalk.domain.animation.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.animation.controller.dto.data.*;
import com.karainc.dailytalk.domain.animation.controller.dto.requset.*;
import com.karainc.dailytalk.domain.animation.controller.dto.response.*;
import com.karainc.dailytalk.domain.animation.entity.AniCategory;
import com.karainc.dailytalk.domain.animation.entity.AniData;
import com.karainc.dailytalk.domain.animation.entity.Animation;
import com.karainc.dailytalk.domain.animation.repository.AniCategoryRepository;
import com.karainc.dailytalk.domain.animation.repository.AniDataRepository;
import com.karainc.dailytalk.domain.animation.repository.AnimationRepository;
import com.karainc.dailytalk.domain.contents.repository.SubConMemberRepository;
import com.karainc.dailytalk.domain.contents.service.ContentService;
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
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AnimationService {
    private final AdminService adminService;
    private final AnimationRepository animationRepository;
    private final AniCategoryRepository aniCategoryRepository;
    private final AniDataRepository aniDataRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final S3UploadService s3UploadService;
    private final ContentService contentService;


    // 애니 초안
    public MakeResultDto makeAni(String adminKey,MakeDto makeDto){
        adminService.checkAdmin(adminKey);
        AniCategory aniCategory = aniCategoryRepository.findByAniCategoryIdx(makeDto.getAniCategoryIdx());
        if(aniCategory == null){
            throw new NoDataExceptionHandler("애니메이션 카테고리를 먼저 만들어주세요");
        }

        List<String> newAniList = new ArrayList<>();
        List<String> aniName = new ArrayList<>();

        Animation animation = Animation.builder()
                .aniTitle(makeDto.getTitle())
                .aniCategory(aniCategory)
                .animations(newAniList)
                .aniName(aniName)
                .view(false)
                .build();

        Long nowAniID = animationRepository.save(animation).getAniIdx();
        MakeResultDto makeResultDto = new MakeResultDto();
        makeResultDto.setStatus("200");
        makeResultDto.setMessage("애니메이션 초안 작성 완료");
        makeResultDto.setData(nowAniID);
        return makeResultDto;
    }


    // 애니 작성
    public ResultDto newAni(String adminKey, AnimakeDto animakeDto){
        adminService.checkAdmin(adminKey);
        Animation animation = animationRepository.findByAniIdx(animakeDto.getAniIdx());
        if(animation == null){
            throw new NoDataExceptionHandler("없는 애니메이션 입니다 먼저 애니메이션 카테고리와 제목을 작성해야합니다");
        }

        if(animakeDto.getAniName()==null || animakeDto.getAnimations()==null){
            throw new DataNotMatchHandler("작성 오류: 애니메이션 링크와 이름을 작성해주세요");
        }

        List<String> getAnis = animation.getAnimations();
        List<String> getAniNames= animation.getAniName();

        getAnis.add(animakeDto.getAnimations());
        getAniNames.add(animakeDto.getAniName());

        animation.setAnimations(getAnis);
        animation.setAniName(getAniNames);

        log.info("ani link: " +animakeDto.getAnimations()+" " +"ani name: "+animakeDto.getAniName() );
        log.info("애니메이션 위치 : " + String.valueOf(animation.getAniIdx()));

        animationRepository.save(animation);

        ResultDto resultDto =new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("애니메이션 추가 완료");
        resultDto.setData("애니메이션 추가 완료");
        return resultDto;
    }


    // 애니 서브타이틀 , 레벨 나이 설정
    public ResultDto setAni(String adminKey, AniSetDto aniSetDto){
        adminService.checkAdmin(adminKey);
        Animation animation = animationRepository.findByAniIdx(aniSetDto.getAniIdx());
        if(animation == null){
            throw new NoDataExceptionHandler("없는 퀴즈 입니다");
        }
        String getST;
        String getL;
        String getA;
        if(aniSetDto.getAniAge()==null || aniSetDto.getAniAge().isEmpty()){
            getA=animation.getAniAge();
        }else{
            getA= aniSetDto.getAniAge();
        }
        if(aniSetDto.getAniLevel()==null || aniSetDto.getAniLevel().isEmpty()){
            getL=animation.getAniLevel();
        }else{
            getL= aniSetDto.getAniLevel();
        }
        if(aniSetDto.getAniSubTitle()==null || aniSetDto.getAniSubTitle().isEmpty()){
            getST = animation.getAniSubTitle();
        }else{
            getST = aniSetDto.getAniSubTitle();
        }

        log.info("애니 레벨 설정");
        log.info(getST);
        log.info(getA);
        log.info(getL);
        log.info("애니 위치 : " + String.valueOf(animation.getAniIdx()));

        animation.setAniLevel(getL);
        animation.setAniAge(getA);
        animation.setAniSubTitle(getST);
        animationRepository.save(animation);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("애니 레벨 설정 완료(관리자 권한)");
        resultDto.setData("애니 레벨 설정 완료(관리자 권한)");
        return resultDto;
    }

    // 애니 부분 수정
    public ResultDto updateAni(String adminKey, AniUpdateDto aniUpdateDto){
        adminService.checkAdmin(adminKey);
        Animation animation = animationRepository.findByAniIdx(aniUpdateDto.getAniIdx());
        if(animation==null){
            throw new DataNotMatchHandler("없는 애니 ID 입니다");
        }


        List<String> getAniName= animation.getAniName();
        List<String> getAnis = animation.getAnimations();

        getAniName.set(aniUpdateDto.getAniNumber(), aniUpdateDto.getAniName());
        getAnis.set(aniUpdateDto.getAniNumber(), aniUpdateDto.getAnimations());

        animation.setAnimations(getAnis);
        animation.setAniName(getAniName);
        animationRepository.save(animation);

        log.info("애니 부분수정");
        log.info(aniUpdateDto.getAnimations());
        log.info(aniUpdateDto.getAniName());
        log.info("수정할 위치 : " + String.valueOf(animation.getAniIdx()));

        ResultDto resultDto =new ResultDto();
        resultDto.setStatus("200");
        resultDto.setData("애니 수정 완료(관리자 권한)");
        resultDto.setMessage("애니 수정 완료(관리자 권한)");
        return resultDto;
    }

    // 애니 부분 삭제
    public ResultDto partAniDelete(String adminKey, Long aniIdx, int aniNumber){
        adminService.checkAdmin(adminKey);
        Animation animation = animationRepository.findByAniIdx(aniIdx);
        if(animation==null){
            throw new NoDataExceptionHandler("이미 지워진 데이터 입니다");
        }

        List<String> getAniName= animation.getAniName();
        List<String> getAnis = animation.getAnimations();

        getAniName.remove(aniNumber);
        getAnis.remove(aniNumber);

        log.info("부분 삭제 애니 위치 : "+String.valueOf(aniIdx));
        animationRepository.save(animation);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("애니 부분 삭제완료(관리자 권한)");
        resultDto.setData("애니 부분 삭제완료(관리자 권한)");
        return resultDto;
    }


    // 애니 완전 삭제
    public ResultDto deleteAni(String adminKey, long aniIdx){
        adminService.checkAdmin(adminKey);
        Animation animation = animationRepository.findByAniIdx(aniIdx);
        if(animation==null){
            throw new NoDataExceptionHandler("이미 지워진 데이터 입니다");
        }

        log.info("완전 삭제 애니 위치 : "+String.valueOf(aniIdx));

        animationRepository.delete(animation);
        s3UploadService.deleteFolder("ani/"+String.valueOf(animation.getAniIdx()));

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("관리자 권한으로 삭제 완료");
        resultDto.setData("관리자 권한으로 삭제 완료");
        return resultDto;
    }

    // 타이틀 변경
    public ResultDto changeTitleName(String adminKey, AniTitleDto aniTitleDto){
        adminService.checkAdmin(adminKey);
        Animation animation = animationRepository.findByAniIdx(aniTitleDto.getAniIdx());
        if(animation == null){
            throw new NoDataExceptionHandler("없는 애니 입니다");
        }
        String a;
        if(aniTitleDto.getAniTitle()==null || aniTitleDto.getAniTitle().isEmpty()){
            a=animation.getAniTitle();
        }else{
            a=aniTitleDto.getAniTitle();
        }


        log.info("애니 타이틀 수정 : " + aniTitleDto.getAniTitle());
        log.info("애니 수정 위치 : " + String.valueOf(aniTitleDto.getAniTitle()));

        animation.setAniTitle(a);
        animationRepository.save(animation);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("애니 타이틀 변경 (관리자 권한)");
        resultDto.setData("애니 타이틀 변경 (관리자 권한)");

        return resultDto;
    }

    // 타이틀 이미지 변경
    public ResultDto changeTitleImage(String adminKey, long aniIdx, MultipartFile image){
        adminService.checkAdmin(adminKey);
        Animation animation = animationRepository.findByAniIdx(aniIdx);
        String imageName;
        if(image==null || image.isEmpty()){
            log.info("이미지 없음");
            imageName="null";
        }else if(image.getSize()>30000000) {
            throw new DataNotMatchHandler("30MB 크기는 업로드 할 수 없습니다");
        } else{
            log.info("이미지 추가");
            String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));

            // 오늘 날짜를 포함한 새로운 파일 이름 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date()) + fileExtension;
            imageName=s3UploadService.uploadImage(image,"quiz/"+String.valueOf(aniIdx)+"/"+newFileName);
        }

        log.info("애니 이미지 수정 위치 : "  + String.valueOf(aniIdx));

        animation.setAniImage(imageName);
        animationRepository.save(animation);

        ResultDto resultDto =new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("애니 이미지 대문 변경 완료");
        resultDto.setData("애니 이미지 대문 변경 완료");
        return resultDto;
    }

    //애니 리스트 조회
    public AniInfoListDto getAniList(String adminKey){
        log.info("애니 목록 보기");
        adminService.checkAdmin(adminKey);
        List<Animation> animations = animationRepository.findAll();
        if(animations==null){
            throw new NoDataExceptionHandler("등록된 애니메이션이 없습니다");
        }
        List<AniInfoDto> aniInfoDtos = new ArrayList<>();

        for(Animation ani : animations){
            long getC;
            String getN;
            if(ani.getAniCategory()==null){
                getC=0;
                getN="빈 카테고리";
            }else {
                getC=ani.getAniCategory().getAniCategoryIdx();
                getN=ani.getAniCategory().getAniCategoryName();
            }
            AniInfoDto aniInfoDto = new AniInfoDto();
            aniInfoDto.setAniIdx(ani.getAniIdx());
            aniInfoDto.setAniCategoryIdx(getC);
            aniInfoDto.setAniCategory(getN);
            aniInfoDto.setQCount(ani.getAnimations().size());
            aniInfoDto.setAniTitle(ani.getAniTitle());
            aniInfoDto.setTitleImage(ani.getAniImage());
            aniInfoDto.setAniSubTitle(ani.getAniSubTitle());
            aniInfoDto.setAniAge(ani.getAniAge());
            aniInfoDto.setAniLevel(ani.getAniLevel());
            aniInfoDto.setView(ani.isView());
            aniInfoDtos.add(aniInfoDto);
        }

        AniInfoListDto aniInfoListDto = new AniInfoListDto();
        aniInfoListDto.setStatus("200");
        aniInfoListDto.setMessage("애니 리스트 조회(관리자 권한)");
        aniInfoListDto.setData(aniInfoDtos);

        return aniInfoListDto;
    }

    // 상제 조회
    public ResultAniInfo getForAdmin(String adminKey, Long aniIdx){
        adminService.checkAdmin(adminKey);

        Animation animation = animationRepository.findByAniIdx(aniIdx);
        if(animation==null){
            throw new NoDataExceptionHandler("애니메이션이 없습니다");
        }
        log.info("애니메이션 정보 열람: " + String.valueOf(aniIdx));

        long aniC;
        String ann;
        if(animation.getAniCategory()==null){
            aniC=0;
            ann="빈 카테고리";
        }else{
            aniC = animation.getAniCategory().getAniCategoryIdx();
            ann = animation.getAniCategory().getAniCategoryName();
        }
        AniInfoDetail aniInfoDetail = new AniInfoDetail();
        List<AniDetail> aniDetails = new ArrayList<>();

        for(int i=0; i<animation.getAnimations().size(); i++){
            AniDetail aniDetail = new AniDetail();
            aniDetail.setAnimations(animation.getAnimations().get(i));
            aniDetail.setAniName(animation.getAniName().get(i));
            aniDetails.add(aniDetail);
        }

        aniInfoDetail.setAniIdx(animation.getAniIdx());
        aniInfoDetail.setAniCategoryIdx(aniC);
        aniInfoDetail.setAniCategory(ann);
        aniInfoDetail.setAniTitle(animation.getAniTitle());
        aniInfoDetail.setAniSubTitle(animation.getAniSubTitle());
        aniInfoDetail.setAniLevel(animation.getAniLevel());
        aniInfoDetail.setAniAge(animation.getAniAge());
        aniInfoDetail.setTitleImage(animation.getAniImage());
        aniInfoDetail.setQna(aniDetails);

        ResultAniInfo resultAniInfo = new ResultAniInfo();
        resultAniInfo.setStatus("200");
        resultAniInfo.setMessage("애니메이션 상세 정보");
        resultAniInfo.setData(aniInfoDetail);
        return resultAniInfo;
    }

    // 카테고리 수정
    public ResultDto input(String adminKey, InputCtDto inputCtDto){
        adminService.checkAdmin(adminKey);
        AniCategory aniCategory = aniCategoryRepository.findByAniCategoryIdx(inputCtDto.getAniCategoryIdx());
        if(aniCategory == null){
            throw new NoDataExceptionHandler("없는 카테고리 입니다");
        }
        Animation animation = animationRepository.findByAniIdx(inputCtDto.getAniIdx());
        if(animation==null){
            throw new NoDataExceptionHandler("없는 애니메이션 입니다");
        }



        log.info("애니메이션 카테고리 변경");
        log.info("애니메이션 위치 : " + String.valueOf(animation.getAniIdx()));
        log.info("애니메이션 변경 카테고리 : " + aniCategory.getAniCategoryName());

        animation.setAniCategory(aniCategory);
        animationRepository.save(animation);
        ResultDto resultDto = new ResultDto();

        resultDto.setStatus("200");
        resultDto.setMessage("애니메이션 카테고리 변경 완료(관리자 권한)");
        resultDto.setMessage("애니메이션 카테고리 변경 완료(관리자 권한");
        return resultDto;
    }

    public NowAniDto getNowAniDashBoard(String adminKey, Long aniIdx){
        adminService.checkAdmin(adminKey);

        List<AniData> aniDataList = aniDataRepository.findByAniRef(aniIdx);

        int k  = aniDataList.size();
        System.out.println(k);
        if(k==0){
            throw new NoDataExceptionHandler("애니메이션 현황 내역이 없습니다");
        }

        List<NowAniMember> nowAniMemberList= new ArrayList<>();
        for(AniData aniData : aniDataList){

            NowAniMember nowAniMember = new NowAniMember();
            nowAniMember.setMemberId(aniData.getMember().getMemberId());
            nowAniMember.setName(aniData.getMember().getName());
            nowAniMember.setDate(aniData.getViewDate().toString());
            nowAniMember.setReplayCount(aniData.getReplayCount());
            nowAniMember.setProfile(aniData.getMember().getProfile());
            nowAniMember.setPlayTime(aniData.getPlayTime().stream().mapToInt(Integer::intValue).sum());
            Integer replayCount= aniData.getReplayCount();
            if(replayCount==null || replayCount ==0){
                nowAniMember.setReplayCount(0);
            }else {
                nowAniMember.setReplayCount(replayCount);
            }
            Integer outCount= aniData.getOutCount();
            if(outCount==null || outCount==0){
                nowAniMember.setOutCount(0);
            }else {
                nowAniMember.setOutCount(outCount);
            }
            Integer myRate = aniData.getRate().stream().mapToInt(Integer::intValue).sum();
            double average = (double) myRate / aniData.getRate().size();
            double getMyRate = Math.round(average * 100.0) / 100.0;
            nowAniMember.setRate(String.format("%.2f",getMyRate));
            nowAniMemberList.add(nowAniMember);
        }

        Collections.reverse(nowAniMemberList);

        NowAniDto nowAniDto = new NowAniDto();
        nowAniDto.setStatus("200");
        nowAniDto.setMessage("애니메이션 현황표");
        nowAniDto.setData(nowAniMemberList);

        return nowAniDto;
    }

    // 출력설정
    public ResultDto openClose(String adminKey,OpenAndCloseDto openAndCloseDto){
        adminService.checkAdmin(adminKey);
        Animation animation = animationRepository.findByAniIdx(openAndCloseDto.getAniIdx());
        if(animation==null){
            throw new NoDataExceptionHandler("없는 애니매이션 입니다");
        }
        boolean status;
        String message;
        if(animation.isView()==Boolean.FALSE){
            status=true;
            message="애니메이션 배포 완료";
        }else {
            status=false;
            message="애니메이션 배포 중지";
        }

        animation.setView(status);
        animationRepository.save(animation);

        log.info("애니메이션 위치: " + String.valueOf(animation.getAniIdx()));
        log.info(message);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage(message);
        resultDto.setData(message);
        return resultDto;
    }

    public CtAniListDto getCategoryAdmin(String adminKey, long categoryIdx){
        adminService.checkAdmin(adminKey);
        AniCategory aniCategory = aniCategoryRepository.findByAniCategoryIdx(categoryIdx);
        if(aniCategory==null){
            throw new NoDataExceptionHandler("없는 카테고리 입니다");
        }
        List<Animation> animations = animationRepository.findByAniCategory(aniCategory);
        if(animations==null){
            throw new NoDataExceptionHandler("해당 카테고리는 준비중입니다");
        }

        log.info("애니 카테고리 키워드로 애니리스트 조회");
        log.info("애니 카테고리 위치: " + String.valueOf(categoryIdx));
        log.info("애니 카테고리 이름: "+ aniCategory.getAniCategoryName());

        List<CtList> ctLists = new ArrayList<>();
        for(Animation ani : animations){
            CtList ctList = new CtList();
            ctList.setCategoryIdx(ani.getAniCategory().getAniCategoryIdx());
            ctList.setAniIdx(ani.getAniIdx());
            ctList.setAniTitle(ani.getAniTitle());
            ctList.setAniSubTitle(ani.getAniSubTitle());
            ctList.setAniAge(ani.getAniAge());
            ctList.setAniLevel(ani.getAniLevel());
            ctList.setTitleImage(ani.getAniImage());
            ctList.setView(ani.isView());
            ctList.setQCount(ani.getAnimations().size());
            ctLists.add(ctList);
        }

        CtAniListDto ctAniListDto = new CtAniListDto();
        ctAniListDto.setStatus("200");
        ctAniListDto.setMessage("카테고리로 애니 리스트 조회");
        ctAniListDto.setData(ctLists);
        return ctAniListDto;
    }

    public AniInfoListDto getAniAll(){
        List<Animation> animations = animationRepository.findAll();
        if(animations==null){
            throw new NoDataExceptionHandler("등록된 애니메이션이 없습니다");
        }
        List<AniInfoDto> aniInfoDtos = new ArrayList<>();
        int i =0;
        for(Animation ani : animations){
            if(ani.getAniCategory()!=null && ani.isView()==Boolean.TRUE){
                AniInfoDto aniInfoDto = new AniInfoDto();
                aniInfoDto.setAniIdx(ani.getAniIdx());
                aniInfoDto.setAniCategoryIdx(ani.getAniCategory().getAniCategoryIdx());
                aniInfoDto.setAniCategory(ani.getAniCategory().getAniCategoryName());
                aniInfoDto.setQCount(ani.getAnimations().size());
                aniInfoDto.setAniTitle(ani.getAniTitle());
                aniInfoDto.setTitleImage(ani.getAniImage());
                aniInfoDto.setAniSubTitle(ani.getAniSubTitle());
                aniInfoDto.setAniAge(ani.getAniAge());
                aniInfoDto.setAniLevel(ani.getAniLevel());
                aniInfoDto.setView(ani.isView());
                aniInfoDtos.add(aniInfoDto);
                i = i+1;
            }
        }
        if(i==0){
            throw new NoDataExceptionHandler("애니메이션 준비중입니다");
        }

        log.info("회원 서비스 : 애니 리스트조회 (누구든지 조회 가능)");
        log.info("조건 : 배포된 애니");

        AniInfoListDto aniInfoListDto = new AniInfoListDto();
        aniInfoListDto.setStatus("200");
        aniInfoListDto.setMessage("애니 리스트 조회");
        aniInfoListDto.setData(aniInfoDtos);

        return aniInfoListDto;
    }

    public CtAniListDto getMemCt(long aniCategoryIdx){
        AniCategory aniCategory = aniCategoryRepository.findByAniCategoryIdx(aniCategoryIdx);
        if(aniCategory==null){
            throw new NoDataExceptionHandler("존재하지 않는 카테고리 입니다");
        }
        List<Animation> animations = animationRepository.findByAniCategory(aniCategory);



        int i=0;
        List<CtList> ctLists = new ArrayList<>();
        for(Animation ani : animations){
            if(ani.isView()==Boolean.TRUE){
                CtList ctList = new CtList();
                ctList.setCategoryIdx(ani.getAniCategory().getAniCategoryIdx());
                ctList.setAniIdx(ani.getAniIdx());
                ctList.setAniTitle(ani.getAniTitle());
                ctList.setAniSubTitle(ani.getAniSubTitle());
                ctList.setAniAge(ani.getAniAge());
                ctList.setAniLevel(ani.getAniLevel());
                ctList.setTitleImage(ani.getAniImage());
                ctList.setView(ani.isView());
                ctList.setQCount(ani.getAnimations().size());
                ctLists.add(ctList);
                i = i+1;
            }
        }

        if(i==0){
            throw new NoDataExceptionHandler("애니메이션 준비중입니다");
        }

        log.info("회원 애니 카테고리 키워드로 애니리스트 조회");
        log.info("회원 애니 카테고리 위치: " + String.valueOf(aniCategoryIdx));
        log.info("회원 애니 카테고리 이름: "+ aniCategory.getAniCategoryName());

        CtAniListDto ctAniListDto = new CtAniListDto();
        ctAniListDto.setStatus("200");
        ctAniListDto.setMessage("카테고리로 애니 리스트 조회");
        ctAniListDto.setData(ctLists);
        return ctAniListDto;
    }

    public ResultAniInfo getForMember(String memberKey, Long aniIdx){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);

        contentService.findSubMember(member);

        Animation animation = animationRepository.findByAniIdx(aniIdx);
        if(animation==null){
            throw new NoDataExceptionHandler("애니메이션이 없습니다");
        }

        if(animation.getAniCategory()==null || animation.isView()==Boolean.FALSE){
            throw new NoDataExceptionHandler("현재 애니메이션 수정중 입니다");
        }

        AniInfoDetail aniInfoDetail = new AniInfoDetail();
        List<AniDetail> aniDetails = new ArrayList<>();
        for(int i=0; i<animation.getAnimations().size(); i++){
            AniDetail aniDetail = new AniDetail();
            aniDetail.setAnimations(animation.getAnimations().get(i));
            aniDetail.setAniName(animation.getAniName().get(i));
            aniDetails.add(aniDetail);
        }
        aniInfoDetail.setAniIdx(animation.getAniIdx());
        aniInfoDetail.setAniCategoryIdx(animation.getAniCategory().getAniCategoryIdx());
        aniInfoDetail.setAniCategory(animation.getAniCategory().getAniCategoryName());
        aniInfoDetail.setAniTitle(animation.getAniTitle());
        aniInfoDetail.setAniSubTitle(animation.getAniSubTitle());
        aniInfoDetail.setAniLevel(animation.getAniLevel());
        aniInfoDetail.setAniAge(animation.getAniAge());
        aniInfoDetail.setTitleImage(animation.getAniImage());
        aniInfoDetail.setQna(aniDetails);

        log.info("회원 애니메이션 정보 열람: " + String.valueOf(aniIdx));

        ResultAniInfo resultAniInfo = new ResultAniInfo();
        resultAniInfo.setStatus("200");
        resultAniInfo.setMessage("애니메이션 상세 정보");
        resultAniInfo.setData(aniInfoDetail);
        return resultAniInfo;
    }


    public ResultDto saveRunningTime(String memberKey,PlayTimeDto playTimeDto){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);
        contentService.findSubMember(member);

        Animation animation = animationRepository.findByAniIdx(playTimeDto.getAniIdx());
        if(animation == null || animation.isView()==Boolean.FALSE){
            throw new NoDataExceptionHandler("없는 애니메이션 이거나 현제 중단된 컨텐츠 입니다");
        }

        List<AniData> aniData = aniDataRepository.findByMember(member);
        int k=aniData.size();
        log.info("회원 기록 조회");
        System.out.println(k);
        if(k!=0){
            for(AniData myData : aniData){
                if(myData.getAniRef().toString().equals(playTimeDto.getAniIdx().toString())){
                    log.info("같음");
                    if(myData.getViewDate().toString().equals(LocalDate.now().toString())){
                        log.info("같은날 동일한 애니메이션 시청 알고리즘 실행");
                        List<Integer> getPlayTime = myData.getPlayTime();
                        List<Integer> getRate =myData.getRate();
                        for(int i=0; i< getPlayTime.size(); i++){
                            Integer getTime = getPlayTime.get(i)+playTimeDto.getPlayTime().get(i);
                            Integer getR = getRate.get(i)+playTimeDto.getRate().get(i);
                            if(getR>100){
                                getR=100;
                            }
                            getPlayTime.set(i,getTime);
                            getRate.set(i,getR);
                        }
                        myData.setReplayCount(myData.getReplayCount()+1);
                        myData.setPlayTime(getPlayTime);
                        myData.setRate(getRate);
                        aniDataRepository.save(myData);
                        ResultDto resultDto = new ResultDto();
                        resultDto.setStatus("200");
                        resultDto.setMessage("애니메이션 시청이 완료 되었습니다");
                        resultDto.setData("애니메이션 시청이 완료 되었습니다");

                        return  resultDto;
                    }
                }
            }
        }

        log.info("신규 데이터 기록");
        AniData getAniData = AniData.builder().
                    playTime(playTimeDto.getPlayTime()).
                    rate(playTimeDto.getRate()).
                    outCount(0).
                    replayCount(0).
                    viewDate(LocalDate.now()).
                    aniRef(animation.getAniIdx()).
                    member(member).
                    build();
            aniDataRepository.save(getAniData);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("애니메이션 시청이 완료 되었습니다");
        resultDto.setData("애니메이션 시청이 완료 되었습니다");

        return  resultDto;
    }

    public void StopAnimation(String memberKey, StopAniDto stopAniDto){
        memberService.checkMember(memberKey);

        Member member = memberRepository.findByMemberKey(memberKey);

        contentService.findSubMember(member);

        Animation animation = animationRepository.findByAniIdx(stopAniDto.getAniIdx());
        if(animation==null || animation.isView()==Boolean.FALSE){
            throw new NoDataExceptionHandler("없는 애니메이션 이거나 중단된 애니메이션 입니다");
        }

        List<AniData> aniData = aniDataRepository.findByMember(member);
        int k = aniData.size();
        if(k !=0){
            for(AniData myAniData : aniData){
                if(myAniData.getAniRef().toString().equals(stopAniDto.getAniIdx().toString())){
                    if(myAniData.getViewDate().toString().equals(LocalDate.now().toString())){
                        log.info("같음");
                        myAniData.setOutCount(myAniData.getOutCount()+1);
                        aniDataRepository.save(myAniData);
                        break;
                    }
                }
            }
        }else{
            List<Integer> playTime = new ArrayList<>();
            List<Integer> rate = new ArrayList<>();
            for(int i=0; i<animation.getAnimations().size(); i++){
                playTime.add(0);
                rate.add(0);
            }
            AniData getAniData = AniData.builder()
                    .aniRef(stopAniDto.getAniIdx())
                    .outCount(1)
                    .playTime(playTime)
                    .rate(rate)
                    .member(member)
                    .viewDate(LocalDate.now())
                    .build();
            aniDataRepository.save(getAniData);
        }
    }

    public AniConResultDto myData(String memberKey){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);

        List<AniData> aniData = aniDataRepository.findByMember(member);
        int k=aniData.size();
        if(k==0){
            throw new NoDataExceptionHandler("시청 내역이 없습니다");
        }

        LocalDate today = LocalDate.now();

        List<MyContentsInfo> myContentsInfos= new ArrayList<>();
        for(AniData myAniData : aniData){
            Animation animation = animationRepository.findByAniIdx(myAniData.getAniRef());
            if(animation!=null){

                LocalDate oneYearAgo = today.minusYears(1);
                LocalDate oneYearLater = today.plusYears(1);

                if(!myAniData.getViewDate().isBefore(oneYearAgo) && !myAniData.getViewDate().isAfter(oneYearLater)){
                    log.info("1년치 정보");
                    MyContentsInfo myContentsInfo = new MyContentsInfo();
                    myContentsInfo.setAniIdx(animation.getAniIdx());
                    myContentsInfo.setCategoryName(animation.getAniCategory().getAniCategoryName());
                    myContentsInfo.setAniName(animation.getAniTitle());
                    myContentsInfo.setDate(myAniData.getViewDate().toString());
                    myContentsInfo.setPlayTime(myAniData.getPlayTime().stream().mapToInt(Integer::intValue).sum());
                    Integer replayCount= myAniData.getReplayCount();
                    if(replayCount==null || replayCount ==0){
                        myContentsInfo.setReplayCount(0);
                    }else {
                        myContentsInfo.setReplayCount(replayCount);
                    }
                    Integer outCount= myAniData.getOutCount();
                    if(outCount==null || outCount==0){
                        myContentsInfo.setOutCount(0);
                    }else {
                        myContentsInfo.setOutCount(outCount);
                    }
                    Integer myRate = myAniData.getRate().stream().mapToInt(Integer::intValue).sum();
                    double average = (double) myRate / myAniData.getRate().size();
                    double getMyRate = Math.round(average * 100.0) / 100.0;
                    myContentsInfo.setRate(String.format("%.2f",getMyRate));
                    myContentsInfos.add(myContentsInfo);
                }
            }
        }
        Collections.reverse(myContentsInfos);

        AniConResultDto aniConResultDto = new AniConResultDto();
        aniConResultDto.setStatus("200");
        aniConResultDto.setMessage("애니메이션 정보 : 삭제된 애니메이션은 집계되지 않습니다");
        aniConResultDto.setData(myContentsInfos);

        return aniConResultDto;
    }

}
