package com.karainc.dailytalk.domain.behavior.service;

import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.behavior.controller.dto.data.AllData;
import com.karainc.dailytalk.domain.behavior.controller.dto.data.BeData;
import com.karainc.dailytalk.domain.behavior.controller.dto.data.MyBeData;
import com.karainc.dailytalk.domain.behavior.controller.dto.data.MyData;
import com.karainc.dailytalk.domain.behavior.controller.dto.requset.*;
import com.karainc.dailytalk.domain.behavior.controller.dto.response.AdminBeDto;
import com.karainc.dailytalk.domain.behavior.controller.dto.response.BeMsgDto;
import com.karainc.dailytalk.domain.behavior.controller.dto.response.UserBeDto;
import com.karainc.dailytalk.domain.behavior.entity.BehavResult;
import com.karainc.dailytalk.domain.behavior.entity.Behavior;
import com.karainc.dailytalk.domain.behavior.repository.BehavRepository;
import com.karainc.dailytalk.domain.behavior.repository.BehaviorRepository;
import com.karainc.dailytalk.domain.contents.service.ContentService;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.MemberAuthorizedHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BehaviorService {
    private final MemberRepository memberRepository;
    private final AdminService adminService;
    private final BehaviorRepository behaviorRepository;
    private final BehavRepository behavRepository;
    private final ContentService contentService;


    // 생성
    public BeMsgDto create(String adminKey){
        adminService.checkAdmin(adminKey);

        List<String> getBe = new ArrayList<>();
        List<String> getE = new ArrayList<>();

        Behavior behavior =Behavior.builder()
                .behaviors(getBe)
                .behaviorEnum(getE)
                .duringDate("30")
                .build();

        behaviorRepository.save(behavior);

        BeMsgDto beMsgDto = new BeMsgDto();
        beMsgDto.setStatus("200");
        beMsgDto.setMessage("행동문제 진단 검사 초안 작성");
        beMsgDto.setData("행동문제 진단 검사 초안 작성");
        return beMsgDto;
    }

    // 추가
    public BeMsgDto plusBe(String adminKey, PlusBeDto plusBeDto){
        adminService.checkAdmin(adminKey);

        long i =1;
        Behavior behavior = behaviorRepository.findByBehaviorId(i);

        if(behavior==null){
            throw new NoDataExceptionHandler("먼저 언어진단 검사 초안을 작성해주세요");
        }

        List<String> getB = behavior.getBehaviors();
        List<String> getE = behavior.getBehaviorEnum();

        if(plusBeDto.getBehaviors()==null || plusBeDto.getBehaviors().isEmpty()){
            throw  new NoDataExceptionHandler("언어진단 질문을 작성해주세요");
        }

        getB.add(plusBeDto.getBehaviors());

        if(plusBeDto.getBehaviorEnum()==null || plusBeDto.getBehaviorEnum().isEmpty()) {
            throw new NoDataExceptionHandler("해당하는 질문의 유형을 기입해주세요");
        } else if(plusBeDto.getBehaviorEnum().equals("00") || plusBeDto.getBehaviorEnum().equals("01") ||plusBeDto.getBehaviorEnum().equals("02")
                    || plusBeDto.getBehaviorEnum().equals("03") || plusBeDto.getBehaviorEnum().equals("04")){
                getE.add(plusBeDto.getBehaviorEnum());
        } else {
            throw new NoDataExceptionHandler("해당하는 지문의 유형을 올바르게 기입해주세요");
        }


        behavior.setBehaviors(getB);
        behavior.setBehaviorEnum(getE);

        behaviorRepository.save(behavior);

        BeMsgDto beMsgDto = new BeMsgDto();
        beMsgDto.setStatus("200");
        beMsgDto.setMessage("행동문제 진단 검사 지문 추가 완료");
        beMsgDto.setData("행동문제 진단 검사 지문 추가 완료");
        return beMsgDto;
    }

    // 수정
    public BeMsgDto updateBe(String adminKey, UpdateBeDto updateBeDto){
        adminService.checkAdmin(adminKey);

        long i =1 ;
        Behavior behavior = behaviorRepository.findByBehaviorId(i);
        if(behavior==null){
            throw new NoDataExceptionHandler("현제 작성된 언어문제 진단검사가 없습니다");
        }

        List<String> getB = behavior.getBehaviors();
        List<String> getE = behavior.getBehaviorEnum();
        String getD = behavior.getDuringDate();

//        if((updateBeDto.getBehaviors()==null || updateBeDto.getBehaviors().isEmpty())
//            && (updateBeDto.getBehaviorEnum()==null || updateBeDto.getBehaviorEnum().isEmpty())){
//            throw new NoDataExceptionHandler("변경 하고자 하는 정보가 없습니다");
//        }

        if(updateBeDto.getBehaviors() !=null){
            getB.set(updateBeDto.getIndex(), updateBeDto.getBehaviors());
        }

//        if(updateBeDto.getBehaviorEnum() !=null){
//            getE.set(updateBeDto.getIndex(), updateBeDto.getBehaviorEnum());
//        }
        if(updateBeDto.getBehaviorEnum() !=null){
            if(updateBeDto.getBehaviorEnum().equals("00") || updateBeDto.getBehaviorEnum().equals("01") || updateBeDto.getBehaviorEnum().equals("02") || updateBeDto.getBehaviorEnum().equals("03") ||updateBeDto.getBehaviorEnum().equals("04")){
                getE.set(updateBeDto.getIndex(), updateBeDto.getBehaviorEnum());
            }else {
                throw new NoDataExceptionHandler("해당하는 지문의 유형을 올바르게 기입해주세요");
            }
        }

        if(updateBeDto.getDuringDate() !=null){
            getD = updateBeDto.getDuringDate().toString();
        }

        String limit;
        if(updateBeDto.getLimitDate()==null || updateBeDto.getLimitDate().isEmpty()){
            limit=behavior.getLimitDate();
        }else {
            limit=updateBeDto.getLimitDate();
        }

        behavior.setBehaviors(getB);
        behavior.setBehaviorEnum(getE);
        behavior.setDuringDate(getD);
        behavior.setLimitDate(limit);

        behaviorRepository.save(behavior);

        BeMsgDto beMsgDto = new BeMsgDto();
        beMsgDto.setStatus("200");
        beMsgDto.setMessage("행동문제 진단 검사 수정 완료");
        beMsgDto.setData("행동문제 진단 검사 수정 완료");
        return beMsgDto;
    }

    // 부분 삭제
    public BeMsgDto deleteBe(String adminKey, DeleteBeDto deleteBeDto){
        adminService.checkAdmin(adminKey);

        long i=1;

        Behavior behavior = behaviorRepository.findByBehaviorId(i);

        if(behavior==null){
            throw new NoDataExceptionHandler("작성된 진단 검사가 없습니다");
        }

        List<String> getB = behavior.getBehaviors();
        List<String> getE =behavior.getBehaviorEnum();

        getB.remove(deleteBeDto.getIndex());
        getE.remove(deleteBeDto.getIndex());

        behavior.setBehaviors(getB);
        behavior.setBehaviorEnum(getE);

        behaviorRepository.save(behavior);

        BeMsgDto beMsgDto = new BeMsgDto();
        beMsgDto.setStatus("200");
        beMsgDto.setMessage("행동문제 진단 검사 부분 삭제 완료");
        beMsgDto.setData("행동문제 진단 검사 부분 삭제 완료");
        return beMsgDto;
    }

    //조회(관리자 권한)
    public AdminBeDto forAdmin(String adminKey){
        adminService.checkAdmin(adminKey);

        long i=1;

        Behavior behavior = behaviorRepository.findByBehaviorId(i);

        if(behavior == null){
            throw new NoDataExceptionHandler("작성된 행동문제 진단 검사가 없습니다");
        }

        BeData beData = new BeData();
        beData.setBehaviors(behavior.getBehaviors());
        beData.setBehaviorsEnum(behavior.getBehaviorEnum());
        beData.setDuringDate(behavior.getDuringDate());
        beData.setLimitDate(behavior.getLimitDate());

        AdminBeDto adminBeDto = new AdminBeDto();
        adminBeDto.setStatus("200");
        adminBeDto.setMessage("언어문제 진단검사 정보 : 관리자 권한");
        adminBeDto.setData(beData);

        return adminBeDto;
    }

    //조회(회원 권한)
    public UserBeDto forMember(String memberKey){
        if(memberKey == null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }

        Member member = memberRepository.findByMemberKey(memberKey);

        if(member==null){
            throw new NoDataExceptionHandler("회원 전용 서비스 입니다");
        }

        contentService.findCheckMember(member);

        long i=1;

        Behavior behavior = behaviorRepository.findByBehaviorId(i);

        if(behavior == null){
            throw new NoDataExceptionHandler("작성된 행동문제 진단 검사가 없습니다");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String limit = behavior.getLimitDate();
        LocalDate now = LocalDate.now();
        LocalDate getLimit = LocalDate.parse(limit,formatter);

        if(now.equals(getLimit)){
            throw new DataNotMatchHandler("해당 검사의 유통 기간은 " + limit+ " 까지 입니다");
        }

        List<BehavResult> behavResults = behavRepository.findByMember(member);
        int p= behavResults.size();
        if(p!=0){

            String lastTest =behavResults.get(p-1).getNextDate();
            LocalDate last = LocalDate.parse(lastTest, formatter);

            if(now.isBefore(last)){
                throw new NoDataExceptionHandler("이미 검사한 회원 입니다 다음 검사일정을 기다려주세요");
            }
        }

        BeData beData = new BeData();
        beData.setBehaviors(behavior.getBehaviors());
        beData.setDuringDate(behavior.getDuringDate());

        UserBeDto userBeDto = new UserBeDto();
        userBeDto.setStatus("200");
        userBeDto.setMessage("언어문제 진단검사 조회");
        userBeDto.setData(beData);

        return userBeDto;
    }

    //제출(회원 권한)
    public BeMsgDto review(String memberKey, BehavReDto behavReDto){
        if(memberKey == null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }

        Member member = memberRepository.findByMemberKey(memberKey);

        if(member==null){
            throw new NoDataExceptionHandler("회원 전용 서비스 입니다");
        }

        contentService.findCheckMember(member);

        long i=1;

        Behavior behavior = behaviorRepository.findByBehaviorId(i);

        if(behavior == null){
            throw new NoDataExceptionHandler("작성된 행동문제 진단 검사가 없습니다");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String limit = behavior.getLimitDate();
        LocalDate now = LocalDate.now();
        LocalDate getLimit = LocalDate.parse(limit,formatter);

        if(now.equals(getLimit)){
            throw new DataNotMatchHandler("해당 검사의 유통 기간은 " + limit+ " 까지 입니다");
        }

        List<BehavResult> behavResults = behavRepository.findByMember(member);
        int p= behavResults.size();
        if(p!=0){


            String lastTest =behavResults.get(p-1).getNextDate();

            LocalDate last = LocalDate.parse(lastTest, formatter);

            if(now.isBefore(last)){
                throw new NoDataExceptionHandler("이미 검사한 회원 입니다 다음 검사일정을 기다려주세요");
            }
        }

        if(behavReDto==null || behavReDto.getBehav().isEmpty()){
            throw new NoDataExceptionHandler("검사를 진행해주세요");
        }else if(behavReDto.getBehav().size() != behavior.getBehaviors().size()){
            throw new NoDataExceptionHandler("모든 지문에 응답해주세요");
        }

        List<String> getF = Arrays.asList("0","0","0","0","0");
        List<String> getS = Arrays.asList("0","0","0","0","0");

        for(int k =0; k<behavReDto.getBehav().size(); k++){
            if(behavReDto.getBehav().get(k)>4 || behavReDto.getBehav().get(k)<0){
                throw new NoDataExceptionHandler("외부 입력 오류");
            }
            if(behavReDto.getBehav().get(k)!=0 && behavReDto.getBehav().get(k)!=1){
                Integer getIdx = Integer.parseInt(behavior.getBehaviorEnum().get(k));
                Integer stick = Integer.parseInt(getS.get(getIdx))+(behavReDto.getBehav().get(k)-1);
                Integer five = Integer.parseInt(getF.get(getIdx))+1;
                getS.set(getIdx,String.valueOf(stick));
                getF.set(getIdx,String.valueOf(five));
            }
        }

        BehavResult behavResult = BehavResult.builder()
                .duringDate(behavior.getDuringDate())
                .behavDate(LocalDate.now().toString())
                .nextDate(LocalDate.now().plusDays(Integer.parseInt(behavior.getDuringDate())).toString())
                .behavPoint(getF)
                .behavStick(getS)
                .member(member)
                .build();

        behavRepository.save(behavResult);

        contentService.minusCountCheck(member);


        BeMsgDto beMsgDto = new BeMsgDto();
        beMsgDto.setStatus("200");
        beMsgDto.setMessage("행동문제 진단 검사 완료");
        beMsgDto.setData("행동문제 진단 검사 완료");
        return beMsgDto;
    }

    // 응시 결과 조회(회원 권한)
    public MyDataDto getMyDashB(String memberKey){
        if(memberKey == null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }

        Member member = memberRepository.findByMemberKey(memberKey);

        if(member==null){
            throw new NoDataExceptionHandler("회원 전용 서비스 입니다");
        }

        List<BehavResult> behavResults= behavRepository.findByMember(member);
        int l = behavResults.size();
        if(l==0){
            throw new NoDataExceptionHandler("응시 내역이 없습니다");
        }

        List<MyBeData> myBeData = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for(BehavResult behavResult : behavResults){
            LocalDate behavDate = LocalDate.parse(behavResult.getBehavDate(),formatter);

            LocalDate oneYearAgo = today.minusYears(1);
            LocalDate oneYearLater = today.plusYears(1);

            if(!behavDate.isBefore(oneYearAgo) && !behavDate.isAfter(oneYearLater)){
                MyBeData myB = new MyBeData();
                myB.setBehavResultId(behavResult.getBehavResultId());
                myB.setBehavDate(behavResult.getBehavDate());
                myB.setBehavFive(behavResult.getBehavPoint());
                myB.setBehavStick(behavResult.getBehavStick());
                myBeData.add(myB);
            }
        }

        MyData myData = new MyData();
        myData.setMyData(myBeData);
        myData.setNextData(behavResults.get(l-1).getNextDate());

        MyDataDto myDataDto =new MyDataDto();
        myDataDto.setStatus("200");
        myDataDto.setMessage("내 행동문제 검사 진단 내역");
        myDataDto.setData(myData);

        return myDataDto;
    }


    // 전체 인원 응시결과 조회
    public AllMemberDto getAllDashB(String adminKey){
        adminService.checkAdmin(adminKey);

        List<BehavResult> behavResults = behavRepository.findAll();

        List<AllData> allDataList = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for(BehavResult behavResult : behavResults){
            LocalDate behavDate = LocalDate.parse(behavResult.getBehavDate(),formatter);

            LocalDate oneYearAgo = today.minusYears(1);
            LocalDate oneYearLater = today.plusYears(1);


            if(!behavDate.isBefore(oneYearAgo) && !behavDate.isAfter(oneYearLater) && behavResult.getMember()!=null){
                AllData allData = new AllData();
                allData.setName(behavResult.getMember().getName());
                allData.setProfile(behavResult.getMember().getProfile());
                allData.setBehavDate(behavResult.getBehavDate());
                allData.setBehavFive(behavResult.getBehavPoint());
                allData.setBehavStick(behavResult.getBehavStick());
                allData.setNextDate(behavResult.getNextDate());
                allDataList.add(allData);
            }
        }

        AllMemberDto allMemberDto = new AllMemberDto();
        allMemberDto.setStatus("200");
        allMemberDto.setMessage("행동문제 진단 검사 전체 응시 인원 조회");
        allMemberDto.setData(allDataList);

        return allMemberDto;
    }


    public UserBeDto forMemberTest(String memberKey){
        if(memberKey == null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }

        Member member = memberRepository.findByMemberKey(memberKey);

        if(member==null){
            throw new NoDataExceptionHandler("회원 전용 서비스 입니다");
        }

        long i=1;

        Behavior behavior = behaviorRepository.findByBehaviorId(i);

        if(behavior == null){
            throw new NoDataExceptionHandler("작성된 행동문제 진단 검사가 없습니다");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String limit = behavior.getLimitDate();
        LocalDate now = LocalDate.now();
        LocalDate getLimit = LocalDate.parse(limit,formatter);

        if(now.equals(getLimit)){
            throw new DataNotMatchHandler("해당 검사의 유통 기간은 " + limit+ " 까지 입니다");
        }

//        List<BehavResult> behavResults = behavRepository.findByMember(member);
//        int p= behavResults.size();
//        if(p!=0){
//
//            String lastTest =behavResults.get(p-1).getNextDate();
//            LocalDate last = LocalDate.parse(lastTest, formatter);
//
//            if(now.isBefore(last)){
//                throw new NoDataExceptionHandler("이미 검사한 회원 입니다 다음 검사일정을 기다려주세요");
//            }
//        }

        BeData beData = new BeData();
        beData.setBehaviors(behavior.getBehaviors());
        beData.setDuringDate(behavior.getDuringDate());

        UserBeDto userBeDto = new UserBeDto();
        userBeDto.setStatus("200");
        userBeDto.setMessage("언어문제 진단검사 조회");
        userBeDto.setData(beData);

        return userBeDto;
    }

    public BeMsgDto reviewTest(String memberKey, BehavReDto behavReDto){
        if(memberKey == null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }

        Member member = memberRepository.findByMemberKey(memberKey);

        if(member==null){
            throw new NoDataExceptionHandler("회원 전용 서비스 입니다");
        }

        long i=1;

        Behavior behavior = behaviorRepository.findByBehaviorId(i);

        if(behavior == null){
            throw new NoDataExceptionHandler("작성된 행동문제 진단 검사가 없습니다");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String limit = behavior.getLimitDate();
        LocalDate now = LocalDate.now();
        LocalDate getLimit = LocalDate.parse(limit,formatter);

        if(now.equals(getLimit)){
            throw new DataNotMatchHandler("해당 검사의 유통 기간은 " + limit+ " 까지 입니다");
        }

//        List<BehavResult> behavResults = behavRepository.findByMember(member);
//        int p= behavResults.size();
//        if(p!=0){
//
//
//            String lastTest =behavResults.get(p-1).getNextDate();
//
//            LocalDate last = LocalDate.parse(lastTest, formatter);
//
//            if(now.isBefore(last)){
//                throw new NoDataExceptionHandler("이미 검사한 회원 입니다 다음 검사일정을 기다려주세요");
//            }
//        }

        if(behavReDto==null || behavReDto.getBehav().isEmpty()){
            throw new NoDataExceptionHandler("검사를 진행해주세요");
        }else if(behavReDto.getBehav().size() != behavior.getBehaviors().size()){
            throw new NoDataExceptionHandler("모든 지문에 응답해주세요");
        }

        List<String> getF = Arrays.asList("0","0","0","0","0");
        List<String> getS = Arrays.asList("0","0","0","0","0");

        for(int k =0; k<behavReDto.getBehav().size(); k++){
            if(behavReDto.getBehav().get(k)>4 || behavReDto.getBehav().get(k)<0){
                throw new NoDataExceptionHandler("외부 입력 오류");
            }
            if(behavReDto.getBehav().get(k)!=0 && behavReDto.getBehav().get(k)!=1){
                Integer getIdx = Integer.parseInt(behavior.getBehaviorEnum().get(k));
                log.info(getIdx.toString());
                Integer stick = Integer.parseInt(getS.get(getIdx))+(behavReDto.getBehav().get(k)-1);
                log.info(stick.toString());
                Integer five = Integer.parseInt(getF.get(getIdx))+1;
                System.out.println(five);
                getS.set(getIdx,String.valueOf(stick));
                getF.set(getIdx,String.valueOf(five));
                System.out.println(getS);
                System.out.println(getF);
            }
        }

        BehavResult behavResult = BehavResult.builder()
                .duringDate(behavior.getDuringDate())
                .behavDate(LocalDate.now().toString())
                .nextDate(LocalDate.now().plusDays(Integer.parseInt(behavior.getDuringDate())).toString())
                .behavPoint(getF)
                .behavStick(getS)
                .member(member)
                .build();

        behavRepository.save(behavResult);

        BeMsgDto beMsgDto = new BeMsgDto();
        beMsgDto.setStatus("200");
        beMsgDto.setMessage("행동문제 진단 검사 완료");
        beMsgDto.setData("행동문제 진단 검사 완료");
        return beMsgDto;
    }


}
