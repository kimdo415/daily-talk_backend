package com.karainc.dailytalk.domain.member.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karainc.dailytalk.domain.admin.dto.data.MemberTypeDto;
import com.karainc.dailytalk.domain.admin.dto.request.ChangePasswordDto;
import com.karainc.dailytalk.domain.behavior.entity.BehavResult;
import com.karainc.dailytalk.domain.behavior.repository.BehavRepository;
import com.karainc.dailytalk.domain.consult.entity.Consult;
import com.karainc.dailytalk.domain.consult.repository.ConsultRepository;
import com.karainc.dailytalk.domain.contents.entity.CheckConMember;
import com.karainc.dailytalk.domain.contents.entity.ConsultMember;
import com.karainc.dailytalk.domain.contents.entity.SubConMember;
import com.karainc.dailytalk.domain.contents.repository.CheckConMemberRepository;
import com.karainc.dailytalk.domain.contents.repository.ConsultMemberRepository;
import com.karainc.dailytalk.domain.contents.repository.SubConMemberRepository;
import com.karainc.dailytalk.domain.contents.service.ContentService;
import com.karainc.dailytalk.domain.exceptionhandler.*;
import com.karainc.dailytalk.domain.faq.entity.Faq;
import com.karainc.dailytalk.domain.language.entity.LangResult;
import com.karainc.dailytalk.domain.language.repository.LangResultRepository;
import com.karainc.dailytalk.domain.member.dto.data.MemberInfoDetailDto;
import com.karainc.dailytalk.domain.member.dto.data.RehabInfoDetailDto;
import com.karainc.dailytalk.domain.member.dto.request.*;
import com.karainc.dailytalk.domain.member.dto.response.*;
import com.karainc.dailytalk.domain.member.entity.LoginMember;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.entity.Rehabilitator;
import com.karainc.dailytalk.domain.member.repository.LoginMemberRepository;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.member.repository.RehabilitatorRepository;
import com.karainc.dailytalk.domain.quiz.entity.QuizData;
import com.karainc.dailytalk.domain.quiz.repository.QuizDataRepository;
import com.karainc.dailytalk.domain.utils.s3.service.S3UploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private  final RehabilitatorRepository rehabilitatorRepository;
    private final S3UploadService s3UploadService;
    private final LoginMemberRepository loginMemberRepository;
    private final CheckConMemberRepository checkConMemberRepository;
    private final SubConMemberRepository subConMemberRepository;
    private final ConsultMemberRepository consultMemberRepository;
    private final ConsultRepository consultRepository;
    private final QuizDataRepository quizDataRepository;
    private final LangResultRepository langResultRepository;
    private final BehavRepository behavRepository;

    @Transactional
    public JoinResultDto join(MemberDto memberDto){

        if(memberRepository.findByMemberId(memberDto.getMemberId())!=null ||
           memberRepository.findByEmail(memberDto.getEmail()) !=null ||
           memberRepository.findByNickName(memberDto.getNickname()) !=null ||
           memberRepository.findByPhoneNumber(memberDto.getPhoneNumber()) !=null){
            throw new DataRedundancyHandler("데이터 중복 : 아이디/이메일/닉네임/전화번호 중복 검사를 진행해주세요");
        }

        Member member=Member.builder()
                .memberId(memberDto.getMemberId())
                .name(memberDto.getName())
                .nickName(memberDto.getNickname())
                .memberType(memberDto.getMemberType())
                .phoneNumber(memberDto.getPhoneNumber())
                .guardian(memberDto.getGuardian())
                .memberKey(getUUid())
                .profile("null")
                .intro("null")
                .morePn(memberDto.getMorePn())
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .day(LocalDate.now().toString())
                .payStatus(false)
                .loginStatus(true)
                .mkService(Boolean.TRUE)
                .createDay(LocalDate.now())
                .loginType("00")
                .build();

        memberRepository.save(member);

        JoinResultDto joinResultDto = new JoinResultDto();
        joinResultDto.setStatus("200");
        joinResultDto.setMessage("학생/부모 회원가입이 완료 되었습니다");
        joinResultDto.setData("학생/부모 회원가입이 완료 되었습니다");
        return joinResultDto;
    }

    public void checkMember(String memberKey){
        log.info("회원 토큰 검증");
        if(memberKey==null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }
        Member member =memberRepository.findByMemberKey(memberKey);
        if(member == null){
            throw new MemberAuthorizedHandler("없는 회원의 키입니다");
        }
//        if(member.getDState()==Boolean.FALSE){
//            throw new NoDataExceptionHandler("비활성화 된 계정 입니다");
//        }
        if(member.getLoginStatus()==Boolean.FALSE){
            log.info("차단된 회원");
            log.info("차단된 회원 ID: " + member.getMemberId());
            throw new DataNotMatchHandler("차단된 회원입니다");
        }
        if(member.getMemberType().equals("02")){
            Rehabilitator rehabilitator = rehabilitatorRepository.findByRefId(member.getIdx());
            if(!rehabilitator.getCheckEnum().equals("01")){
                throw new NoDataExceptionHandler("승인되지 않는 재활사 입니다");
            }
        }
    }
    @Transactional
    public JoinResultDto rehabJoin(RehabMemberDto rehabMemberDto){

        if(memberRepository.findByMemberId(rehabMemberDto.getMemberId())!=null ||
                memberRepository.findByEmail(rehabMemberDto.getEmail()) !=null ||
                memberRepository.findByNickName(rehabMemberDto.getNickname()) !=null ||
                memberRepository.findByPhoneNumber(rehabMemberDto.getPhoneNumber()) !=null){
            throw new DataRedundancyHandler("데이터 중복 : 아이디/이메일/닉네임/전화번호 중복 검사를 진행해주세요");
        }

        Member member=Member.builder()
                .memberId(rehabMemberDto.getMemberId())
                .password(passwordEncoder.encode(rehabMemberDto.getPassword()))
                .name(rehabMemberDto.getName())
                .nickName(rehabMemberDto.getNickname())
                .phoneNumber(rehabMemberDto.getPhoneNumber())
                .memberType(rehabMemberDto.getMemberType())
                .email(rehabMemberDto.getEmail())
                .intro(rehabMemberDto.getIntro())
                .memberKey(getUUid())
                .profile("null")
                .day(LocalDate.now().toString())
                .payStatus(false)
                .loginStatus(true)
                .customerKey(getCustomerKey())
                .createDay(LocalDate.now())
                .loginType("00")
                .build();


        Rehabilitator rehabilitator=Rehabilitator.builder()
                .region(rehabMemberDto.getActivityArea())
                .cert(rehabMemberDto.getCert())
                .career(rehabMemberDto.getCareer())
                .division(rehabMemberDto.getDivision())
                .careerDay(rehabMemberDto.getCareerDay())
                .status(false)
                .checkEnum("00")
                .day(LocalDate.now().toString())
                .build();

        long ref = memberRepository.save(member).getIdx();
        rehabilitator.setRefId(ref);
        rehabilitatorRepository.save(rehabilitator);


        JoinResultDto joinResultDto = new JoinResultDto();
        joinResultDto.setStatus("200");
        joinResultDto.setMessage("재활사 가입이 완료 되었습니다 가입승인을 기달려주세요");
        joinResultDto.setData("재활사 가입이 완료 되었습니다 가입승인을 기달려주세요");
        return joinResultDto;
        
    }

    public MemberKeyDto login(String memberId, String password){
        Member member = memberRepository.findByMemberId(memberId);
        if(member==null){
            throw new DataNotMatchHandler("없는 아이디 입니다");
        }
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new DataNotMatchHandler("비밀번호가 다릅니다");
        }
        if(member.getLoginStatus()==Boolean.FALSE){
            throw new NoDataExceptionHandler("차단된 회원 입니다");
        }if(member.getDState()==Boolean.FALSE){
            throw new DataNotMatchHandler("비활성화 된 회원 입니다");
        }
//        if(member.getMemberType().equals("02")){
//            Rehabilitator rehabilitator=rehabilitatorRepository.findByRefId(member.getIdx());
//            if(!rehabilitator.getCheckEnum().equals("01")){
//                throw new DataNotMatchHandler("재활사의 경우 관리자의 승인이 필요합니다");
//            }
//        }
        String customerKey;
        if(member.getCustomerKey()==null){
            log.info("새로운 멤버키 생성");
            customerKey=getCustomerKey();
            member.setCustomerKey(customerKey);
            memberRepository.save(member);
        }else {
            log.info("멤버키 있는사람 기존 값");
            customerKey=member.getCustomerKey();
        }

        LocalDate today = LocalDate.now();
        LoginMember loginMember=loginMemberRepository.findByLoginDate(today);
        if(loginMember==null){
            log.info("금일 첫 로그인");
            LoginMember newLogin = LoginMember.builder()
                    .loginDate(today)
                    .loginCount(1)
                    .build();
            loginMemberRepository.save(newLogin);
        }else{
            loginMember.setLoginCount(loginMember.getLoginCount()+1);
            loginMemberRepository.save(loginMember);
        }


        MemberTypeDto memberTypeDto =new MemberTypeDto();
        memberTypeDto.setType(member.getMemberType());
        memberTypeDto.setMemberKey(member.getMemberKey());
        memberTypeDto.setCustomerKey(customerKey);

        MemberKeyDto memberKeyDto = new MemberKeyDto();
        memberKeyDto.setStatus("200");
        memberKeyDto.setMessage("로그인 성공");
        memberKeyDto.setData(memberTypeDto);
        return memberKeyDto;
    }

    public String getUUid(){
        String key;
        while(true){
            UUID uuid = UUID.randomUUID();
            key = uuid.toString().replace("-", "").substring(0, 10);
            //uuid.toString().replace("-", "").substring(0, 10)
            Member member=memberRepository.findByMemberKey(key);
            if(member == null){
                break;
            }
        }
        return key;
    }

    public String getCustomerKey(){
        String customerKey;
        while(true){
            UUID uuid = UUID.randomUUID();
            customerKey = uuid.toString().replace("-", "").substring(0, 12);
            customerKey = customerKey.substring(0,1).toUpperCase()+customerKey.substring(1);
            StringBuffer str = new StringBuffer(customerKey);
            str.insert(5,"_");
            customerKey=str.toString();
            Member member=memberRepository.findByCustomerKey(customerKey);
            if(member == null){
                break;
            }
        }
        return customerKey;
    }

    public ResultDto checkId(String memberId){
        ResultDto resultDto = new ResultDto();
        Member member=memberRepository.findByMemberId(memberId);
        if(!(member==null)){
            throw new DataRedundancyHandler("오류 : 중복된아이디");
        }else {
            resultDto.setStatus("200");
            resultDto.setData("사용 가능한 ID 입니다");
            resultDto.setMessage("사용 가능한 ID");
            return resultDto;
        }
    }


    //coolsms 달야함
    public ResultDto checkPn(String phoneNumber){
        ResultDto resultDto = new ResultDto();
        Member member= memberRepository.findByPhoneNumber(phoneNumber);
        if(!(member==null)){
            throw new DataRedundancyHandler("오류 : 전화번호 중복");
        }else {
            resultDto.setStatus("200");
            resultDto.setData("사용 가능한 전화번호 입니다");
            resultDto.setMessage("사용 가능한 전화번호");
            return resultDto;
        }
    }

    public ResultDto checkNickName(String nickName){
        ResultDto resultDto = new ResultDto();
        Member member = memberRepository.findByNickName(nickName);
        if(!(member==null)){
            throw new DataRedundancyHandler("오류 : 닉네임 중복");
        }else {
            resultDto.setStatus("200");
            resultDto.setData("사용 가능한 닉네임 입니다");
            resultDto.setMessage("사용 가능한 닉네임");
            return resultDto;
        }
    }

    public ResultDto checkEmail(String email){
        ResultDto resultDto = new ResultDto();
        Member member = memberRepository.findByEmail(email);
        if(!(member==null)){
            throw new DataRedundancyHandler("오류 : 이메일 중복");
        }else {
            resultDto.setStatus("200");
            resultDto.setData("사용 가능한 이메일 입니다");
            resultDto.setMessage("사용 가능한 이메일");
            return resultDto;
        }
    }

    public ResultDto findId(String email){
        Member member =memberRepository.findByEmail(email);
        if(member==null){
            throw new DataNotMatchHandler("오류: 해당 이메일로 찾을 수 없습니다");
        }
        if(member.getLoginType().equals("01")){
            throw new DataNotMatchHandler("카카오톡 회원은 조회 불가능합니다");
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setMessage("가입된 회원");
        resultDto.setStatus("200");
        resultDto.setData(member.getMemberId());
        return resultDto;
    }

    public ResultDto findIdByPhone(String phone){
        Member member = memberRepository.findByPhoneNumber(phone);
        if(member==null){
            throw new DataNotMatchHandler("오류: 해당 휴대폰 번호로 찾을 수 없습니다");
        }
        if(member.getLoginType().equals("01")){
            throw new DataNotMatchHandler("카카오톡 회원은 조회 불가능합니다");
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("가입된 회원");
        resultDto.setData(member.getMemberId());
        return resultDto;
    }

    public ResultDto rePassword(String memberKey, ChangePasswordDto changePasswordDto){
        checkMember(memberKey);
        Member member = memberRepository.findByMemberId(changePasswordDto.getMemberId());
        if(member==null){
            throw new NoDataExceptionHandler("없는 회원 ID 입니다");
        }
        if(!member.getMemberId().equals(changePasswordDto.getMemberId())){
            throw new DataNotMatchHandler("회원 토큰값과 아이디 불일치 합니다");
        }
        log.info("회원 정보 변경 : (memberId :%s, memberType : %s",member.getMemberId(),member.getMemberType());
        member.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        memberRepository.save(member);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("비밀번호 변경이 완료되었습니다");
        resultDto.setData("비밀번호 변경이 완료되었습니다");
        return resultDto;
    }

    public MemberInfoDto getInfo(String memberKey){
        checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);
        if(member == null){
            throw new NoDataExceptionHandler("없는 회원 입니다");
        }
        MemberInfoDto memberInfoDto = new MemberInfoDto();


        MemberInfoDetailDto memberInfoDetailDto = new MemberInfoDetailDto();
        memberInfoDetailDto.setMemberIdx(member.getIdx());
        memberInfoDetailDto.setSex(member.getSex());
        memberInfoDetailDto.setMemberId(member.getMemberId());
        memberInfoDetailDto.setSex(member.getSex());
        memberInfoDetailDto.setMemberType(member.getMemberType());
        memberInfoDetailDto.setName(member.getName());
        memberInfoDetailDto.setGuardian(member.getGuardian());
        memberInfoDetailDto.setPhoneNumber(member.getPhoneNumber());
        memberInfoDetailDto.setNickName(member.getNickName());
        memberInfoDetailDto.setMorePn(member.getMorePn());
        memberInfoDetailDto.setProfile(member.getProfile());
        memberInfoDetailDto.setEmail(member.getEmail());
        memberInfoDetailDto.setAddress(member.getAddress());
        memberInfoDetailDto.setBirth(member.getBirth());
        memberInfoDetailDto.setPhone1(member.getPhone1());
        memberInfoDetailDto.setPhone2(member.getPhone2());
        memberInfoDetailDto.setParentsSex(member.getParentSex());
        memberInfoDetailDto.setParentsBirth(member.getParentBirth());
        memberInfoDetailDto.setMemberShip(member.getMemberShip());

        if(member.getMkService()==null){
            memberInfoDetailDto.setMkService(Boolean.TRUE);
        }else{
            memberInfoDetailDto.setMkService(member.getMkService());
        }

        memberInfoDto.setStatus("200");
        memberInfoDto.setMessage("회원 정보 조회");
        memberInfoDto.setData(memberInfoDetailDto);

        return memberInfoDto;
    }

    public RehabInfoDto getRehabInfo(String memberKey){
        if(memberKey==null || memberKey.isEmpty()){
            throw new MemberAuthorizedHandler("토큰 만료");
        }
        Member member = memberRepository.findByMemberKey(memberKey);
        if(member==null){
            throw new NoDataExceptionHandler("없는 회원 입니다");
        }
        if(member.getMemberType().equals("01")){
            throw new DataNotMatchHandler("재활사가 아닌 회원 입니다");
        }

        Rehabilitator rehabilitator = rehabilitatorRepository.findByRefId(member.getIdx());
        RehabInfoDetailDto rehabInfoDetailDto = new RehabInfoDetailDto();
        rehabInfoDetailDto.setMemberIdx(member.getIdx());
        rehabInfoDetailDto.setSex(member.getSex());
        rehabInfoDetailDto.setMemberId(member.getMemberId());
        rehabInfoDetailDto.setName(member.getName());
        rehabInfoDetailDto.setNickName(member.getNickName());
        rehabInfoDetailDto.setEmail(member.getEmail());
        rehabInfoDetailDto.setMemberType(member.getMemberType());
        rehabInfoDetailDto.setPhoneNumber(member.getPhoneNumber());
        rehabInfoDetailDto.setProfile(member.getProfile());
        rehabInfoDetailDto.setIntro(member.getIntro());
        rehabInfoDetailDto.setActivityArea(rehabilitator.getRegion());
        rehabInfoDetailDto.setCert(rehabilitator.getCert());
        rehabInfoDetailDto.setCareer(rehabilitator.getCareer());
        rehabInfoDetailDto.setCareerDay(rehabilitator.getCareerDay());
        rehabInfoDetailDto.setDivision(rehabilitator.getDivision());
        rehabInfoDetailDto.setAddress(member.getAddress());
        rehabInfoDetailDto.setBirth(member.getBirth());
        rehabInfoDetailDto.setCheckEnum(rehabilitator.getCheckEnum());
        rehabInfoDetailDto.setGuardian(member.getGuardian());
        rehabInfoDetailDto.setPhone1(member.getPhone1());
        rehabInfoDetailDto.setParentsSex(member.getParentSex());
        rehabInfoDetailDto.setParentsBirth(member.getParentBirth());
        rehabInfoDetailDto.setPhone2(member.getPhone2());
        rehabInfoDetailDto.setDetailAddress(rehabilitator.getDetailAddress());


        if(member.getMkService()==null){
            rehabInfoDetailDto.setMkService(Boolean.TRUE);
        }else{
            rehabInfoDetailDto.setMkService(member.getMkService());
        }


        RehabInfoDto rehabInfoDto = new RehabInfoDto();
        rehabInfoDto.setStatus("200");
        rehabInfoDto.setMessage("재활사 정보");
        rehabInfoDto.setData(rehabInfoDetailDto);

        return rehabInfoDto;
    }

    public ResultDto changeInfo(String memberKey, UpdateMemberDto updateMemberDto){
        checkMember(memberKey);

        Member member=memberRepository.findByMemberKey(memberKey);
        if(member == null){
            throw new NoDataExceptionHandler("없는 회원입니다");
        }

        System.out.println(updateMemberDto.getMkService());
        member.setSex(updateMemberDto.getSex());
        member.setAddress(updateMemberDto.getAddress());
        member.setBirth(updateMemberDto.getBirth());
        member.setIntro(updateMemberDto.getIntro());
        member.setName(updateMemberDto.getName());
        member.setMorePn(updateMemberDto.getMorePn());
        member.setGuardian(updateMemberDto.getGuardian());
        member.setPhone1(updateMemberDto.getPhone1());
        member.setPhone2(updateMemberDto.getPhone2());
        member.setParentSex(updateMemberDto.getParentsSex());
        member.setParentBirth(updateMemberDto.getParentsBirth());
        member.setMemberShip(updateMemberDto.getMemberShip());
        member.setMkService(updateMemberDto.getMkService());
        memberRepository.save(member);


        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("회원 변경 완료");
        resultDto.setData("회원 변경 완료");
        return resultDto;
    }

    public ResultDto updateReInfo(String memberKey, UpdateRehabDto updateRehabDto){
        if(memberKey==null){
            throw new MemberAuthorizedHandler("토큰 만료");
        }

        Member member=memberRepository.findByMemberKey(memberKey);
        if(!member.getMemberType().equals("02")){
            throw new DataNotMatchHandler("일반 회원은 재활사의 정보에 접근 불가능합니다");
        }

        Rehabilitator rehabilitator = rehabilitatorRepository.findByRefId(member.getIdx());
        if(rehabilitator==null){
            throw new NoDataExceptionHandler("없는 재활사 입니다");
        }
        member.setBirth(updateRehabDto.getBirth());
        member.setSex(updateRehabDto.getSex());
        member.setAddress(updateRehabDto.getAddress());
        member.setIntro(updateRehabDto.getIntro());
        member.setName(updateRehabDto.getName());
        member.setGuardian(updateRehabDto.getGuardian());
        member.setPhone1(updateRehabDto.getPhone1());
        member.setPhone2(updateRehabDto.getPhone2());
        member.setParentSex(updateRehabDto.getSex());
        member.setParentBirth(updateRehabDto.getParentsBirth());
        member.setMkService(updateRehabDto.getMkService());

        rehabilitator.setRegion(updateRehabDto.getActivityArea());
        rehabilitator.setCert(updateRehabDto.getCert());
        rehabilitator.setCareerDay(updateRehabDto.getCareerDay());
        rehabilitator.setCareer(updateRehabDto.getCareer());
        rehabilitator.setDivision(updateRehabDto.getDivision());
        rehabilitator.setDetailAddress(updateRehabDto.getDetailAddress());

        memberRepository.save(member);
        rehabilitatorRepository.save(rehabilitator);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("회원 변경 완료");
        resultDto.setData("회원 변경 완료");

        return resultDto;
    }

//    public ResponseEntity<JSONObject> pay(String memberKey, String jsonBody)throws Exception{
//        log.info("결제테스트");
//        checkMember(memberKey);
//
//        log.info("토큰 검사");
//        Member member = memberRepository.findByMemberKey(memberKey);
//
//        log.info("제이슨 파싱");
//        JSONParser parser = new JSONParser();
//        String orderId;
//        String amount;
//        String paymentKey;
//        try {
//            // 클라이언트에서 받은 JSON 요청 바디입니다.
//            org.json.simple.JSONObject requestData = (JSONObject) parser.parse(jsonBody);
//            paymentKey = (String) requestData.get("paymentKey");
//            orderId = (String) requestData.get("orderId");
//            amount = (String) requestData.get("amount");
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        };
//        JSONObject obj = new JSONObject();
//        obj.put("orderId", orderId);
//        obj.put("amount", amount);
//        obj.put("paymentKey", paymentKey);
//
//
//        log.info("api-키 입력");
//        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
//        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
//        String apiKey = "test_sk_EP59LybZ8BpXyjj1JoXBV6GYo7pR";
//        Base64.Encoder encoder = Base64.getEncoder();
//        byte[] encodedBytes = encoder.encode((apiKey + ":").getBytes("UTF-8"));
//        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);
//
//        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
//        log.info("api 실행");
//        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Authorization", authorizations);
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//
//        OutputStream outputStream = connection.getOutputStream();
//        outputStream.write(obj.toString().getBytes("UTF-8"));
//
//        int code = connection.getResponseCode();
//        log.info("코드값: "+ code);
//        boolean isSuccess = code == 200 ? true : false;
//        if(code ==200){
//            log.info("결제 성공");
//            log.info("결제 한사람의 pk: " + member.getIdx());
//            log.info("결제 한사람의 ID 및 이름" + member.getMemberId() + " " + member.getName());
//            member.setPayStatus(true);
//            memberRepository.save(member);
//        }else{
//            log.info("결제 실패");
//        }
//        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
//
//        log.info("여기까지 왔니?");
//        // 결제 성공 및 실패 비즈니스 로직을 구현하세요.
//        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
//        JSONObject jsonObject = (JSONObject) parser.parse(reader);
//        responseStream.close();
//
//        return ResponseEntity.status(code).body(jsonObject);
//    }

    public ResultDto deleteMember(String memberKey){
        checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);
        if(member==null){
            throw new NoDataExceptionHandler("이미 삭제된 회원 입니다");
        }

        List<Consult>  consults=consultRepository.findByMember(member);

        for(Consult consult:consults){
            consult.setMember(null);
            consultRepository.save(consult);
        }

        List<QuizData> quizData = quizDataRepository.findBymRef(member.getIdx());
        int k =quizData.size();
        if(k !=0){
            for(QuizData quizD : quizData){
                quizDataRepository.delete(quizD);
            }
        }

        List<LangResult> langResults=  langResultRepository.findByMember(member);
        for(LangResult langResult :langResults){
            langResult.setMember(null);
            langResultRepository.save(langResult);
        }
        List<BehavResult> behavResults=behavRepository.findByMember(member);
        for(BehavResult behavResult :behavResults){
            behavResult.setMember(null);
            behavRepository.save(behavResult);
        }
        s3UploadService.deleteFolder("member/"+member.getMemberKey());
        memberRepository.delete(member);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("일반 회원 탈퇴 완료");
        resultDto.setData("일반 회원 탈퇴 완료");
        return resultDto;
    }

    public ResultDto deleteRehabMember(String memberKey){
        checkMember(memberKey);

        Member member = memberRepository.findByMemberKey(memberKey);
        if(member==null){
            throw new NoDataExceptionHandler("이미 삭제된 회원 입니다");
        }

        List<Consult>  consults=consultRepository.findByMember(member);

        for(Consult consult:consults){
            consult.setRehab(null);
            consultRepository.save(consult);
        }

        List<QuizData> quizData = quizDataRepository.findBymRef(member.getIdx());
        int k =quizData.size();
        if(k !=0){
            for(QuizData quizD : quizData){
                quizDataRepository.delete(quizD);
            }
        }

        List<LangResult> langResults=  langResultRepository.findByMember(member);
        for(LangResult langResult :langResults){
            langResult.setMember(null);
            langResultRepository.save(langResult);
        }
        List<BehavResult> behavResults=behavRepository.findByMember(member);
        for(BehavResult behavResult :behavResults){
            behavResult.setMember(null);
            behavRepository.save(behavResult);
        }
        s3UploadService.deleteFolder("member/"+member.getMemberKey());
        memberRepository.delete(member);

        Rehabilitator rehabilitator = rehabilitatorRepository.findByRefId(member.getIdx());

        memberRepository.delete(member);
        rehabilitatorRepository.delete(rehabilitator);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("재활사 회원 탈퇴 완료");
        resultDto.setData("재활사 회원 탈퇴 완료");
        return resultDto;
    }

    public ResultDto changeProfile(String memberKey, MultipartFile newImage){
        checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);
        if(member==null){
            throw new NoDataExceptionHandler("삭제된 회원입니다");
        }
        String imageName;
        if(newImage.getSize()>30000000) {
            throw new DataNotMatchHandler("30MB 크기는 업로드 할 수 없습니다");
        }
        String getImage;
        if(newImage.isEmpty()){
            member.setProfile("null");
            memberRepository.save(member);
        }else{
            String fileExtension = newImage.getOriginalFilename().substring(newImage.getOriginalFilename().lastIndexOf("."));
            // 오늘 날짜를 포함한 새로운 파일 이름 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date()) + fileExtension;
            getImage=s3UploadService.uploadImage(newImage,"member/"+memberKey+"/"+newFileName);
            member.setProfile(getImage);
            memberRepository.save(member);
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("일반 회원 변경 완료");
        resultDto.setData("일반 회원 변경 완료");
        return resultDto;
    }

    public void end(long idx){
        Rehabilitator rehabilitator = rehabilitatorRepository.findByRehabilIdx(idx);
        rehabilitatorRepository.delete(rehabilitator);
    }

    public ModelAndView findKakao(String accessToken){


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response.getBody());
            // 액세스 토큰 추출
            String Id = rootNode.get("id").asText();

//            return ResponseEntity.ok(Id);
            String externalUrl ="http://dailytalk.net/";
            return new ModelAndView("redirect:"+externalUrl);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new DataNotMatchHandler("실패");
        }
    }


    public String findKakaoId(String accessToken){

        log.info("카카오토큰 회원 가입 검증");
        log.info(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response.getBody());
            // 액세스 토큰 추출
            log.info("결과");
            log.info(rootNode.toString());
            String Id = rootNode.get("id").asText();
            log.info(Id);
            Member member = memberRepository.findByMemberId(Id);
            if(member != null){
                throw new DataRedundancyHandler("이미 가입된 카카오 계정 입니다");
            }
            return Id;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new DataNotMatchHandler("실패");
        }

    }



    public KakoToKenDto getCode(String code){
        System.out.println("code = " + code);

        // 1. header 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        // 2. body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 고정값
//        params.add("client_id", "6f30b20460040568aff176bc8ac82ada"); // 내거
        params.add("client_id", "d6389052ff39df26be576d96bbf477e4"); // 카라꺼
        params.add("redirect_uri", "http://dailytalk.net:7713/member/kakao/callback"); // 등록한 redirect uri
        params.add("code", code);

        // 3. header + body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        // ResponseEntity의 body에서 JSON 형태의 응답을 매핑
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response.getBody());
            // 액세스 토큰 추출
            String accessToken = rootNode.get("access_token").asText();
            System.out.println("Access Token: " + accessToken);

            KakoToKenDto kakoToKenDto = new KakoToKenDto();
            kakoToKenDto.setStatus("200");
            kakoToKenDto.setMessage("토큰 정보");
            kakoToKenDto.setData(accessToken);

            return kakoToKenDto;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new DataNotMatchHandler("실패");
        }
    }





    public String getMyId(String accessToken){
        log.info("카카오토큰 회원 가입 검증");
        log.info(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response.getBody());
            // 액세스 토큰 추출
            log.info("결과");
            log.info(rootNode.toString());
            String Id = rootNode.get("id").asText();
            log.info(Id);
            Member member = memberRepository.findByMemberId(Id);
            if(member==null){
                throw new NoDataExceptionHandler("없는 계정 입니다");
            }
            return Id;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new DataNotMatchHandler("실패");
        }
    }
    @Transactional
    public ResultDto KakaoMember(KakakoMemberDto kakakoMemberDto){
        log.info("회원 가입 시도");
        log.info(kakakoMemberDto.getAccessToken());
        String Id = findKakaoId(kakakoMemberDto.getAccessToken());
        Member member=Member.builder()
                .memberId(Id)
                .name(kakakoMemberDto.getName())
                .nickName(kakakoMemberDto.getNickname())
                .memberType(kakakoMemberDto.getMemberType())
                .phoneNumber(kakakoMemberDto.getPhoneNumber())
                .guardian(kakakoMemberDto.getGuardian())
                .memberKey(getUUid())
                .profile("null")
                .intro("null")
                .morePn(kakakoMemberDto.getMorePn())
                .email(kakakoMemberDto.getEmail())
                .day(LocalDate.now().toString())
                .payStatus(false)
                .loginStatus(true)
                .createDay(LocalDate.now())
                .loginType("01")
                .build();

        memberRepository.save(member);


        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("회원 가입 완료");
        resultDto.setData("회원 가입 완료");

        return resultDto;
    }

    @Transactional
    public ResultDto KakaoRehMember(KakaoRehabMemberDto kakaoRehabMemberDto){
        String Id = findKakaoId(kakaoRehabMemberDto.getAccessToken());
        log.info(Id);
        Member member=Member.builder()
                .memberId(Id)
                .name(kakaoRehabMemberDto.getName())
                .nickName(kakaoRehabMemberDto.getNickname())
                .phoneNumber(kakaoRehabMemberDto.getPhoneNumber())
                .memberType(kakaoRehabMemberDto.getMemberType())
                .email(kakaoRehabMemberDto.getEmail())
                .intro(kakaoRehabMemberDto.getIntro())
                .memberKey(getUUid())
                .profile("null")
                .day(LocalDate.now().toString())
                .payStatus(false)
                .loginStatus(true)
                .customerKey(getCustomerKey())
                .createDay(LocalDate.now())
                .loginType("01")
                .build();


        Rehabilitator rehabilitator=Rehabilitator.builder()
                .region(kakaoRehabMemberDto.getActivityArea())
                .cert(kakaoRehabMemberDto.getCert())
                .career(kakaoRehabMemberDto.getCareer())
                .division(kakaoRehabMemberDto.getDivision())
                .careerDay(kakaoRehabMemberDto.getCareerDay())
                .status(false)
                .checkEnum("00")
                .day(LocalDate.now().toString())
                .build();

        long ref = memberRepository.save(member).getIdx();
        rehabilitator.setRefId(ref);
        rehabilitatorRepository.save(rehabilitator);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("재활사 가입이 완료 되었습니다 가입승인을 기달려주세요");
        resultDto.setData("재활사 가입이 완료 되었습니다 가입승인을 기달려주세요");

        return resultDto;
    }


    public MemberKeyDto kakaoLogin(LoginTokenDto loginTokenDto){
        String Id = getMyId(loginTokenDto.getAccessToken());

        Member member = memberRepository.findByMemberId(Id);
        if(member==null){
            throw new DataNotMatchHandler("없는 아이디 입니다");
        }
        if(member.getLoginStatus()==Boolean.FALSE){
            throw new NoDataExceptionHandler("차단된 회원 입니다");
        }
        String customerKey;
        if(member.getCustomerKey()==null){
            log.info("새로운 멤버키 생성");
            customerKey=getCustomerKey();
            member.setCustomerKey(customerKey);
            memberRepository.save(member);
        }else {
            log.info("멤버키 있는사람 기존 값");
            customerKey=member.getCustomerKey();
        }

        LocalDate today = LocalDate.now();
        LoginMember loginMember=loginMemberRepository.findByLoginDate(today);
        if(loginMember==null){
            log.info("금일 첫 로그인");
            LoginMember newLogin = LoginMember.builder()
                    .loginDate(today)
                    .loginCount(1)
                    .build();
            loginMemberRepository.save(newLogin);
        }else{
            loginMember.setLoginCount(loginMember.getLoginCount()+1);
            loginMemberRepository.save(loginMember);
        }

        MemberTypeDto memberTypeDto =new MemberTypeDto();
        memberTypeDto.setType(member.getMemberType());
        memberTypeDto.setMemberKey(member.getMemberKey());
        memberTypeDto.setCustomerKey(customerKey);

        MemberKeyDto memberKeyDto = new MemberKeyDto();
        memberKeyDto.setStatus("200");
        memberKeyDto.setMessage("로그인 성공");
        memberKeyDto.setData(memberTypeDto);
        return memberKeyDto;
    }



}



