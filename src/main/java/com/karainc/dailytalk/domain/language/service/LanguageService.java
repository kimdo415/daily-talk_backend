package com.karainc.dailytalk.domain.language.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.consult.service.ConsultService;
import com.karainc.dailytalk.domain.contents.service.ContentService;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.MemberAuthorizedHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.language.contoller.dto.data.LangData;
import com.karainc.dailytalk.domain.language.contoller.dto.data.LangInfo;
import com.karainc.dailytalk.domain.language.contoller.dto.data.MemberData;
import com.karainc.dailytalk.domain.language.contoller.dto.requset.CallLangDto;
import com.karainc.dailytalk.domain.language.contoller.dto.requset.DeleteLangDto;
import com.karainc.dailytalk.domain.language.contoller.dto.requset.MyLangListDto;
import com.karainc.dailytalk.domain.language.contoller.dto.requset.UpdateLangDto;
import com.karainc.dailytalk.domain.language.contoller.dto.response.AllMemberDto;
import com.karainc.dailytalk.domain.language.contoller.dto.response.LangMsgDto;
import com.karainc.dailytalk.domain.language.contoller.dto.response.LangListDto;
import com.karainc.dailytalk.domain.language.contoller.dto.response.MyLangDto;
import com.karainc.dailytalk.domain.language.entity.LangResult;
import com.karainc.dailytalk.domain.language.entity.Language;
import com.karainc.dailytalk.domain.language.repository.LangResultRepository;
import com.karainc.dailytalk.domain.language.repository.LanguageRepository;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
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
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LangResultRepository langResultRepository;
    private final MemberRepository memberRepository;
    private final AdminService adminService;
    private final ContentService contentService;

    public LangMsgDto create(String adminKey){
        adminService.checkAdmin(adminKey);


        List<String> getL = new ArrayList<>();
        List<String> getLEnum = new ArrayList<>();

        Language language = Language.builder()
                .languages(getL)
                .languageEnum(getLEnum)
                .duringDate("30")
                .build();

        languageRepository.save(language);

        LangMsgDto langMsgDto = new LangMsgDto();
        langMsgDto.setStatus("200");
        langMsgDto.setMessage("언어문제 생성 완료");
        langMsgDto.setData("언어문제 생성 완료");

        return langMsgDto;
    }

    public LangMsgDto plus(String adminKey, CallLangDto callLangDto){
        adminService.checkAdmin(adminKey);

        long i=1;

        Language language = languageRepository.findByLanguageId(i);

        if(language==null){
            throw new NoDataExceptionHandler("없는 언어문제 입니다");
        }

        if(callLangDto.getLanguages()==null || callLangDto.getLanguages().isEmpty()){
            throw new NoDataExceptionHandler("지문을 넣어주세요");
        }

        if(callLangDto.getLanguageEnum()==null || callLangDto.getLanguageEnum().isEmpty()){
            throw new NoDataExceptionHandler("지문에 맞는 유형을 넣어주세요");
        }
        if(Integer.parseInt(callLangDto.getLanguageEnum())<0 || Integer.parseInt(callLangDto.getLanguageEnum())>3 ){
            throw new NoDataExceptionHandler("지문에 대한 유형외의 타입 입니다");
        }

        List<String> getL = language.getLanguages();
        List<String> getE = language.getLanguageEnum();
        getL.add(callLangDto.getLanguages());
        getE.add(callLangDto.getLanguageEnum());

        language.setLanguages(getL);
        language.setLanguageEnum(getE);

        languageRepository.save(language);

        LangMsgDto langMsgDto = new LangMsgDto();
        langMsgDto.setStatus("200");
        langMsgDto.setMessage("언어문제 추가 완료");
        langMsgDto.setData("언어문제 추가 완료");

        return langMsgDto;
    }

    public LangMsgDto update(String adminKey, UpdateLangDto updateLangDto){
        adminService.checkAdmin(adminKey);

        long i=1;
        Language language = languageRepository.findByLanguageId(i);

        if(language==null){
            throw new NoDataExceptionHandler("해당 언어문제를 조회할수 없습니다");
        }

        List<String> getLang = language.getLanguages();
        List<String> getEnums;
        if(language.getLanguageEnum()==null){
            getEnums = new ArrayList<>();
        }else{
            getEnums=language.getLanguageEnum();
        }

        System.out.println(getEnums);

        if(updateLangDto.getLanguages() !=null && updateLangDto.getIndex() !=null) {
            getLang.set(updateLangDto.getIndex(),updateLangDto.getLanguages());
        }

        if(updateLangDto.getLanguageEnum() !=null && updateLangDto.getIndex() !=null){
            if(updateLangDto.getLanguageEnum().equals("00") || updateLangDto.getLanguageEnum().equals("01") || updateLangDto.getLanguageEnum().equals("02") || updateLangDto.getLanguageEnum().equals("03") ){
                getEnums.set(updateLangDto.getIndex(),updateLangDto.getLanguageEnum());
            }else {
                throw new DataNotMatchHandler("올바른 언어문제 유형을 넣어주세요");
            }
        }


        String date;
        if(updateLangDto.getDuringDate()==null){
            date=language.getDuringDate();
        }else{
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate lcd = LocalDate.parse(language.getDuringDate(), formatter);
//            lcd = lcd.plusDays(updateLangDto.getDuringDate());
//            date = lcd.toString();
            date=updateLangDto.getDuringDate().toString();
        }

        String limit;
        if(updateLangDto.getLimitDate()==null || updateLangDto.getLimitDate().isEmpty()){
            limit = language.getLimitDate();
        }else{
            limit = updateLangDto.getLimitDate();
        }

        language.setLanguages(getLang);
        language.setDuringDate(date);
        language.setLimitDate(limit);
        language.setLanguageEnum(getEnums);
        languageRepository.save(language);

        LangMsgDto langMsgDto = new LangMsgDto();
        langMsgDto.setStatus("200");
        langMsgDto.setMessage("언어문제 수정 완료");
        langMsgDto.setData("언어문제 수정 완료");

        return langMsgDto;
    }

    public LangMsgDto partDel(String adminKey, DeleteLangDto deleteLangDto){
        adminService.checkAdmin(adminKey);

        long i=1;
        Language language = languageRepository.findByLanguageId(i);
        if(language==null){
            throw new NoDataExceptionHandler("해당 언어문제를 조회할수 없습니다");
        }

        List<String> getL = language.getLanguages();
        getL.remove(deleteLangDto.getIndex());

        language.setLanguages(getL);

        languageRepository.save(language);

        LangMsgDto langMsgDto = new LangMsgDto();
        langMsgDto.setStatus("200");
        langMsgDto.setMessage("언어문제 부분 삭제 완료");
        langMsgDto.setData("언어문제 부분 삭제 완료");

        return langMsgDto;
    }

    public LangListDto ForAdmin(String adminKey){
        adminService.checkAdmin(adminKey);

        long i =1;

        Language language = languageRepository.findByLanguageId(i);
        if(language==null){
            throw new NoDataExceptionHandler("먼저 언어문제 를 작성해주세요");
        }

        LangInfo langInfo =new LangInfo();
        langInfo.setLanguages(language.getLanguages());
        langInfo.setDuringDate(language.getDuringDate());
        langInfo.setLimitDate(language.getLimitDate());
        langInfo.setLanguageEnum(language.getLanguageEnum());

        LangListDto langListDto = new LangListDto();
        langListDto.setStatus("200");
        langListDto.setMessage("언어문제 조회 : 관리자 권한");
        langListDto.setData(langInfo);

        return langListDto;
    }

    public LangListDto ForMember(String memberKey){

        if(memberKey==null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }


        Member member = memberRepository.findByMemberKey(memberKey);


        if(member==null){
            throw new NoDataExceptionHandler("회원 전용 서비스 입니다");
        }


        contentService.findCheckMember(member);


        long i =1;

        Language language = languageRepository.findByLanguageId(i);
        if(language==null){
            throw new NoDataExceptionHandler("현재 언어문제 준비중 입니다");
        }
        List<LangResult> results = langResultRepository.findByMember(member);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String getLimit = language.getLimitDate();
        LocalDate getLimitDate = LocalDate.parse(getLimit,formatter);

        LocalDate now = LocalDate.now();

        if(now.equals(getLimitDate)){
            throw new DataNotMatchHandler("해당 검사의 유통 기간은 " + getLimitDate+ " 까지 입니다");
        }

        int l=results.size();
        if (l != 0) {
             // 문자열 형태의 날짜 포맷
            String getLastDateStr = results.get(l - 1).getNextDate();
            LocalDate getLastDate = LocalDate.parse(getLastDateStr, formatter);
            if (now.isBefore(getLastDate)) {
                throw new NoDataExceptionHandler("이미 검사한 회원 입니다 다음 검사일정을 기다려주세요");
            }
        }
        LangInfo langInfo =new LangInfo();
        langInfo.setLanguages(language.getLanguages());
        langInfo.setDuringDate(language.getDuringDate());

        LangListDto langListDto = new LangListDto();
        langListDto.setStatus("200");
        langListDto.setMessage("언어문제");
        langListDto.setData(langInfo);

        return langListDto;
    }

    public AllMemberDto getMembersData(String adminKey){
        adminService.checkAdmin(adminKey);

        List<LangResult> results = langResultRepository.findAll();

        List<MemberData> memberDataList = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 문자열 형태의 날짜 포맷

        int k=0;

        for (LangResult langResult : results) {
            // 문자열 형태의 날짜를 LocalDate로 변환
            LocalDate langDate = LocalDate.parse(langResult.getLangDate(), formatter);

            // 오늘로부터 1년 이내인지 확인
            LocalDate oneYearAgo = today.minusYears(1);
            LocalDate oneYearLater = today.plusYears(1);

            if (!langDate.isBefore(oneYearAgo) && !langDate.isAfter(oneYearLater) && langResult.getMember() !=null) {
                MemberData memberData = new MemberData();
                memberData.setName(langResult.getMember().getName());
                memberData.setProfile(langResult.getMember().getProfile());
                memberData.setLangDate(langResult.getLangDate());
                memberData.setNextDate(langResult.getNextDate());
                memberData.setLangPoint(langResult.getLangPoint());
                memberDataList.add(memberData);
                k = k+1;
            }
        }
        if(k==0){
            throw new NoDataExceptionHandler("응시 내역이 없습니다");
        }

        AllMemberDto allMemberDto = new AllMemberDto();
        allMemberDto.setStatus("200");
        allMemberDto.setMessage("전체 응시 인원 조회");
        allMemberDto.setData(memberDataList);
        return allMemberDto;
    }
    public LangMsgDto submit(String memberKey, MyLangListDto myLangListDto){
        if(memberKey==null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }

        Member member = memberRepository.findByMemberKey(memberKey);

        if(member==null){
            throw new NoDataExceptionHandler("회원 전용 서비스 입니다");
        }

        contentService.findSubMember(member);

        long id =1;

        Language language = languageRepository.findByLanguageId(id);
        if(language==null){
            throw new NoDataExceptionHandler("현재 언어문제 준비중 입니다");
        }

        List<String> getS=language.getLanguageEnum();

        if(myLangListDto.getLangList()==null || myLangListDto.getLangList().isEmpty()){
            throw new NoDataExceptionHandler("검사를 진행해주세요");
        }else if(myLangListDto.getLangList().size()!=language.getLanguages().size()){
            throw new NoDataExceptionHandler("모든 항목에 답변해주세요");
        }

        List<LangResult> results = langResultRepository.findByMember(member);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String getLimit = language.getLimitDate();
        LocalDate getLimitDate = LocalDate.parse(getLimit,formatter);

        LocalDate now = LocalDate.now();

        if(now.equals(getLimitDate)){
            throw new DataNotMatchHandler("해당 검사의 유통 기간은 " + getLimitDate+ " 까지 입니다");
        }

        int l=results.size();
        if (l != 0) {

            String getLastDateStr = results.get(l - 1).getNextDate();
            LocalDate getLastDate = LocalDate.parse(getLastDateStr, formatter);


            if (now.isBefore(getLastDate)) {
                throw new NoDataExceptionHandler("이미 검사한 회원 입니다 다음 검사일정을 기다려주세요");
            }

        }

        List<Integer> getLang= myLangListDto.getLangList();
        List<String> getSpoint = Arrays.asList("0","0","0","0");
        int point=0;
        for(int j=0; j<getLang.size(); j++){
            if(getLang.get(j)>3 || getLang.get(j)<0) {
                throw new NoDataExceptionHandler("답안 제출 오류(강제 외부 기입)");
            }else{
                point += getLang.get(j);

                Integer getIdx = Integer.parseInt(getS.get(j));
                Integer getPoint = Integer.parseInt(getSpoint.get(getIdx))+getLang.get(j);
                getSpoint.set(getIdx,getPoint.toString());
            }
        }

        LangResult langResult = LangResult.builder()
                .langDate(LocalDate.now().toString())
                .nextDate(LocalDate.now().plusDays(Integer.parseInt(language.getDuringDate())).toString())
                .member(member)
                .langPoint(String.valueOf(point))
                .langStick(getSpoint)
                .duringDate(language.getDuringDate())
                .build();

        langResultRepository.save(langResult);

        contentService.minusCountCheck(member);

        log.info("채점 완료");
        log.info("응시자 정보");
        log.info(member.getNickName());


        LangMsgDto langMsgDto = new LangMsgDto();
        langMsgDto.setStatus("200");
        langMsgDto.setMessage("언어진단 검사가 완료 되었습니다");
        langMsgDto.setData("언어진단 검사가 완료 되었습니다");

        return langMsgDto;

    }



    public MyLangDto getMyDashBoard(String memberKey){
        if(memberKey==null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }

        Member member = memberRepository.findByMemberKey(memberKey);

        if(member==null){
            throw new NoDataExceptionHandler("없는 회원 입니다");
        }

        List<LangResult> results = langResultRepository.findByMember(member);

        int length = results.size();
        log.info("결과 조회 길이 : " + String.valueOf(length));


        int k=0;

        List<LangData> langData = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 문자열 형태의 날짜 포맷

        for (LangResult langResult : results) {
            // 문자열 형태의 날짜를 LocalDate로 변환
            LocalDate langDate = LocalDate.parse(langResult.getLangDate(), formatter);

            // 오늘로부터 1년 이내인지 확인
            LocalDate oneYearAgo = today.minusYears(1);
            LocalDate oneYearLater = today.plusYears(1);

            if (!langDate.isBefore(oneYearAgo) && !langDate.isAfter(oneYearLater)) {
                LangData get = new LangData();
                get.setLangResultId(langResult.getLangResultId());
                get.setLangPoint(langResult.getLangPoint());
                get.setLangStick(langResult.getLangStick());
                get.setLangDate(langResult.getLangDate());
                langData.add(get);
                k = k+1;
            }
        }

//        for(LangResult langResult : results){
//            LangData get =new LangData();
//            get.setLangDate(langResult.getLangDate());
//            get.setLangPoint(langResult.getLangPoint());
//            get.setLangDate(langResult.getLangDate());
//            k= k+1;
//            langData.add(get);
//        }

        if(k==0){
            throw new NoDataExceptionHandler("응시 내역이 없습니다");
        }

        long i=1;


        MyLangDto myLangDto = new MyLangDto();
        myLangDto.setStatus("200");
        myLangDto.setMessage("내 응시 내역 조회");
        myLangDto.setData(langData);
        myLangDto.setNextDate(results.get(results.size()-1).getNextDate());

        return myLangDto;
    }

    public LangMsgDto testSub(String memberKey, MyLangListDto myLangListDto){
        if(memberKey==null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }

        Member member = memberRepository.findByMemberKey(memberKey);

        if(member==null){
            throw new NoDataExceptionHandler("회원 전용 서비스 입니다");
        }



        long id =1;

        Language language = languageRepository.findByLanguageId(id);
        if(language==null){
            throw new NoDataExceptionHandler("현재 언어문제 준비중 입니다");
        }

        List<String> getS=language.getLanguageEnum();


        if(myLangListDto.getLangList()==null || myLangListDto.getLangList().isEmpty()){
            throw new NoDataExceptionHandler("검사를 진행해주세요");
        }else if(myLangListDto.getLangList().size()!=language.getLanguages().size()){
            throw new NoDataExceptionHandler("모든 항목에 답변해주세요");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 문자열 형태의 날짜 포맷
        String getLimit = language.getLimitDate();
        LocalDate getLimitDate = LocalDate.parse(getLimit,formatter);

        LocalDate now = LocalDate.now();

        if(now.equals(getLimitDate)){
            throw new DataNotMatchHandler("해당 검사의 유통 기간은 " + getLimitDate+ " 까지 입니다");
        }

        List<Integer> getL= myLangListDto.getLangList();
        List<String> getSpoint = Arrays.asList("0","0","0","0");
        int point=0;
        for(int j=0; j<getL.size(); j++){
            if(getL.get(j)>3 || getL.get(j)<0) {
                throw new NoDataExceptionHandler("답안 제출 오류(강제 외부 기입)");
            }else{
                point += getL.get(j);

                Integer getIdx = Integer.parseInt(getS.get(j));
                Integer getPoint = Integer.parseInt(getSpoint.get(getIdx))+getL.get(j);
                getSpoint.set(getIdx,getPoint.toString());
            }
        }

        LangResult langResult = LangResult.builder()
                .langDate(LocalDate.now().toString())
                .nextDate(LocalDate.now().plusDays(Integer.parseInt(language.getDuringDate())).toString())
                .member(member)
                .langPoint(String.valueOf(point))
                .langStick(getSpoint)
                .duringDate(language.getDuringDate())
                .build();

        langResultRepository.save(langResult);

        log.info("채점 완료");
        log.info("응시자 정보");
        log.info(member.getNickName());


        LangMsgDto langMsgDto = new LangMsgDto();
        langMsgDto.setStatus("200");
        langMsgDto.setMessage("언어진단 검사가 완료 되었습니다");
        langMsgDto.setData("언어진단 검사가 완료 되었습니다");

        return langMsgDto;
    }

    public LangListDto testView(String memberKey){
        if(memberKey==null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }


        Member member = memberRepository.findByMemberKey(memberKey);


        if(member==null){
            throw new NoDataExceptionHandler("회원 전용 서비스 입니다");
        }

        long i =1;

        Language language = languageRepository.findByLanguageId(i);
        if(language==null){
            throw new NoDataExceptionHandler("현재 언어문제 준비중 입니다");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 문자열 형태의 날짜 포맷



        String getLimit = language.getLimitDate();
        LocalDate getLimitDate = LocalDate.parse(getLimit,formatter);

        LocalDate now = LocalDate.now();

        log.info("서버 시간");
        log.info(now.toString());
        log.info(getLimitDate.toString());

        if(now.equals(getLimitDate)){
            throw new DataNotMatchHandler("해당 검사의 유통 기간은 " + getLimitDate+ " 까지 입니다");
        }

        LangInfo langInfo =new LangInfo();
        langInfo.setLanguages(language.getLanguages());
        langInfo.setDuringDate(language.getDuringDate());

        LangListDto langListDto = new LangListDto();
        langListDto.setStatus("200");
        langListDto.setMessage("언어문제");
        langListDto.setData(langInfo);

        return langListDto;
    }


}
