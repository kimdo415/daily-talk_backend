package com.karainc.dailytalk.domain.consult.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.consult.controller.dto.data.ApplyDetailDto;
import com.karainc.dailytalk.domain.consult.controller.dto.data.GetApplyDto;
import com.karainc.dailytalk.domain.consult.controller.dto.data.MyApplyDto;
import com.karainc.dailytalk.domain.consult.controller.dto.data.RehabList;
import com.karainc.dailytalk.domain.consult.controller.dto.requset.ApplyDto;
import com.karainc.dailytalk.domain.consult.controller.dto.requset.CompleteAppDto;
import com.karainc.dailytalk.domain.consult.controller.dto.response.*;
import com.karainc.dailytalk.domain.consult.entity.Consult;
import com.karainc.dailytalk.domain.consult.repository.ConsultRepository;
import com.karainc.dailytalk.domain.contents.entity.CheckConMember;
import com.karainc.dailytalk.domain.contents.repository.CheckConMemberRepository;
import com.karainc.dailytalk.domain.contents.repository.ContentsRepository;
import com.karainc.dailytalk.domain.contents.service.ContentService;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.entity.Rehabilitator;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.member.repository.RehabilitatorRepository;
import com.karainc.dailytalk.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConsultService {
    private final MemberRepository memberRepository;
    private final RehabilitatorRepository rehabilitatorRepository;
    private final AdminService adminService;
    private final MemberService memberService;
    private final ConsultRepository consultRepository;
    private final ContentService contentService;

    public RehabListDto getRehabList(){
        List<Member> members = memberRepository.findByMemberType("02");
        int i=0;

        List<RehabList> rehabLists = new ArrayList<>();
        for(Member member : members){
            Rehabilitator rehabilitator = rehabilitatorRepository.findByRefId(member.getIdx());
            if(rehabilitator.getCheckEnum().equals("01")){
                i = i+1;
                RehabList rehabList = new RehabList();
                rehabList.setMemberIdx(member.getIdx());
                rehabList.setRehabProfile(member.getProfile());
                rehabList.setName(member.getName());
                rehabList.setDivision(rehabilitator.getDivision());
                rehabList.setActivityArea(rehabilitator.getRegion());
                rehabList.setIntro(member.getIntro());
                rehabList.setCert(rehabilitator.getCert());
                rehabList.setCareerDay(rehabilitator.getCareerDay());
                rehabList.setCareer(rehabilitator.getCareer());
                rehabLists.add(rehabList);
            }
        }
        if(i==0){
            throw new NoDataExceptionHandler("현제 재활사 등록중에 있습니다");
        }
        RehabListDto rehabListDto= new RehabListDto();
        rehabListDto.setStatus("200");
        rehabListDto.setMessage("언어재활사 목록 조회");
        rehabListDto.setData(rehabLists);
        return rehabListDto;
    }

    public ResultApplyDto apply(String memberKey, ApplyDto applyDto){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);

        log.info("상담 신청 : 회원 결제 유무 및 재활사간 신청 여부 확인");
        if(member.getMemberType().equals("02")){
            throw new NoDataExceptionHandler("재활사 끼리는 신청할 수 없습니다");
        }

        contentService.findConsultMember(member);

        log.info("상담 신청 : 재활사 관련 정보 체크");
        Member reMember = memberRepository.findByIdx(applyDto.getMemberIdx());

        if(!reMember.getMemberType().equals("02")){
            throw new NoDataExceptionHandler("해당 회원은 재활사가 아닙니다");
        }

        Rehabilitator rehabilitator = rehabilitatorRepository.findByRefId(reMember.getIdx());
        if(!rehabilitator.getCheckEnum().equals("01")){
            throw new DataNotMatchHandler("승인되지 않은 재활사 입니다");
        }

        Consult consult = Consult.builder()
                .rehab(reMember)
                .member(member)
                .startDate(LocalDate.now().toString())
                .endDate(null)
                .view(Boolean.FALSE)
                .build();
        consultRepository.save(consult);

        contentService.minusCountConsult(member);

        log.info("상담신청 회원 이름: " + member.getName());
        log.info("신청 재활사 이름: " +reMember.getName());

        ResultApplyDto resultApplyDto = new ResultApplyDto();
        resultApplyDto.setStatus("200");
        resultApplyDto.setMessage("재활사 상담신청이 완료되었습니다");
        resultApplyDto.setData("재활사 상담신청이 완료 되었습니다");
        return resultApplyDto;
    }

    public MyApplyResultDto getMyApply(String memberKey){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);

        List<Consult> ctMember = consultRepository.findByMember(member);
        if(ctMember.size()==0){
            throw new NoDataExceptionHandler("상담 신청 내역이 없습니다");
        }
        int i=0;
        List<MyApplyDto> myApplyDtoList =new ArrayList<>();
        for(Consult consult : ctMember){
            MyApplyDto myApplyDto = new MyApplyDto();
            myApplyDto.setConsultIdx(consult.getConsultIdx());
            myApplyDto.setRehabName(consult.getRehab().getName());
            myApplyDto.setStartDate(consult.getStartDate());
            myApplyDto.setEndDate(consult.getEndDate());
            myApplyDto.setStatus(consult.isView());
            myApplyDto.setDetail(consult.getDetail());
            myApplyDtoList.add(myApplyDto);
        }

        Collections.reverse(myApplyDtoList);

        log.info("상담 신청 내역 조회");
        log.info("회원 위치 : " +String.valueOf(member.getMemberId()));

        MyApplyResultDto myApplyResultDto = new MyApplyResultDto();
        myApplyResultDto.setStatus("200");
        myApplyResultDto.setMessage("내 상담신청 내역 조회");
        myApplyResultDto.setData(myApplyDtoList);
        return myApplyResultDto;
    }

    public AdminAppDto getAdminApply(String adminKey){
        adminService.checkAdmin(adminKey);
        List<Consult> consults =consultRepository.findAll();
        List<GetApplyDto> gta = new ArrayList<>();
        for(Consult consult : consults){
            if(consult.getMember() !=null){
                GetApplyDto getApplyDto = new GetApplyDto();
                getApplyDto.setConsultIdx(consult.getConsultIdx());
                getApplyDto.setRehabIdx(consult.getRehab().getIdx());
                getApplyDto.setRehabProfile(consult.getRehab().getProfile());
                getApplyDto.setRehabName(consult.getRehab().getName());
                getApplyDto.setMemberIdx(consult.getMember().getIdx());
                getApplyDto.setMemberProfile(consult.getMember().getProfile());
                getApplyDto.setMemberName(consult.getMember().getName());
                getApplyDto.setStartDate(consult.getStartDate());
                getApplyDto.setEndDate(consult.getEndDate());
                getApplyDto.setStatus(consult.isView());
                gta.add(getApplyDto);
            }
        }

        Collections.reverse(gta);

        log.info("상담 신청 목록 조회(관리자 권한)");

        AdminAppDto adminAppDto = new AdminAppDto();
        adminAppDto.setStatus("200");
        adminAppDto.setMessage("상담 신청 목록 조회(관리자 권한)");
        adminAppDto.setData(gta);
        return  adminAppDto;
    }

    public DetailDto getConsultDetail(String adminKey,Long consultIdx){
        adminService.checkAdmin(adminKey);

        Consult consult = consultRepository.findByConsultIdx(consultIdx);
        if(consult==null){
            throw new NoDataExceptionHandler("삭제된 상담 입니다");
        }

        ApplyDetailDto applyDetailDto = new ApplyDetailDto();
        applyDetailDto.setConsultIdx(consultIdx);
        applyDetailDto.setRehabName(consult.getRehab().getName());
        applyDetailDto.setStatus(consult.isView());
        applyDetailDto.setDetail(consult.getDetail());

        DetailDto detailDto = new DetailDto();
        detailDto.setStatus("200");
        detailDto.setMessage("상담 내용 상세 보기");
        detailDto.setData(applyDetailDto);
        return detailDto;
    }

    public MsgDto completeConsult(String adminKey, CompleteAppDto completeAppDto){
        adminService.checkAdmin(adminKey);
        Consult consult = consultRepository.findByConsultIdx(completeAppDto.getConsultIdx());

        if(consult==null){
            throw new NoDataExceptionHandler("없는 상담신청입니다");
        }
        consult.setEndDate(LocalDate.now().toString());
        consult.setView(Boolean.TRUE);
        consult.setDetail(completeAppDto.getDetail());
        consultRepository.save(consult);

        log.info("상담 완료(관리자 권한)");
        log.info("상담 위치 : " + String.valueOf(consult.getConsultIdx()));

        MsgDto msgDto = new MsgDto();
        msgDto.setStatus("200");
        msgDto.setMessage("상담 완료(관리자 권한)");
        msgDto.setData("상담 완료(관리자 권한)");
        return msgDto;
    }
}
