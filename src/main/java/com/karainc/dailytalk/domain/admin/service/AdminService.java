package com.karainc.dailytalk.domain.admin.service;


import com.karainc.dailytalk.domain.admin.dto.request.*;
import com.karainc.dailytalk.domain.admin.dto.response.*;
import com.karainc.dailytalk.domain.admin.entity.Admin;
import com.karainc.dailytalk.domain.admin.repository.AdminRepository;
import com.karainc.dailytalk.domain.behavior.entity.BehavResult;
import com.karainc.dailytalk.domain.behavior.repository.BehavRepository;
import com.karainc.dailytalk.domain.consult.entity.Consult;
import com.karainc.dailytalk.domain.consult.repository.ConsultRepository;
import com.karainc.dailytalk.domain.contents.repository.CheckConMemberRepository;
import com.karainc.dailytalk.domain.contents.repository.ConsultMemberRepository;
import com.karainc.dailytalk.domain.contents.repository.SubConMemberRepository;
import com.karainc.dailytalk.domain.contents.service.ContentService;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.DataRedundancyHandler;
import com.karainc.dailytalk.domain.exceptionhandler.DomainUnAuthorizedHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.faq.entity.Faq;
import com.karainc.dailytalk.domain.language.entity.LangResult;
import com.karainc.dailytalk.domain.language.repository.LangResultRepository;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.entity.Rehabilitator;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.member.repository.RehabilitatorRepository;
import com.karainc.dailytalk.domain.quiz.entity.QuizData;
import com.karainc.dailytalk.domain.quiz.repository.QuizDataRepository;
import com.karainc.dailytalk.domain.utils.s3.service.S3UploadService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RehabilitatorRepository rehabilitatorRepository;
    private final S3UploadService s3UploadService;
    private final CheckConMemberRepository checkConMemberRepository;
    private final SubConMemberRepository subConMemberRepository;
    private final ConsultMemberRepository consultMemberRepository;
    private final ConsultRepository consultRepository;
    private final QuizDataRepository quizDataRepository;
    private final LangResultRepository langResultRepository;
    private final BehavRepository behavRepository;



    public void checkAdmin(String adminKey){
        log.info("관리자 토큰 검증");
        if(adminKey==null ||adminKey.isEmpty()){
            throw new DomainUnAuthorizedHandler("토큰 만료");
        }
        Admin admin = adminRepository.findByAdminKey(adminKey);
        if(admin==null){
            throw new DomainUnAuthorizedHandler("유효하지 않은 관리자 토큰");
        }
    }
    @Transactional
    public ResultJoinDto joinAdmin(AdminDto adminDto){
        System.out.println("admin 값 들어옴: " + adminDto.getId());
        Admin admin = adminRepository.findByAdminId(adminDto.getId());
        if(!(admin==null)){
            throw new DataRedundancyHandler("관리자 이이디 중복");
        }

        Admin newAdmin =Admin.builder()
                .adminId(adminDto.getId())
                .adminPwd(passwordEncoder.encode(adminDto.getPassword()))
                .adminKey(getAdminUUid())
                .build();

        adminRepository.save(newAdmin).getAdminIdx();

        ResultJoinDto resultJoinDto = new ResultJoinDto();
        resultJoinDto.setStatus("200");
        resultJoinDto.setMessage("관리자 회원가입 완료");
        resultJoinDto.setData("관리자 회원가입 완료");
        return resultJoinDto;
    }

    public String getAdminUUid(){
        String key;
//        do{
//            UUID uuid = UUID.randomUUID();
//            key = uuid.toString().replace("-", "").substring(0, 10);
//        }while(!(memberRepository.);
        while(true){
            UUID uuid = UUID.randomUUID();
            key = uuid.toString().replace("-", "").substring(0, 11);
            Admin admin=adminRepository.findByAdminKey(uuid.toString().replace("-", "").substring(0, 10));
            break;
        }
        return key;
    }


    public AdminKey adminLogin(AdminLoginDto adminLoginDto){
        Admin admin = adminRepository.findByAdminId(adminLoginDto.getId());
        if(admin==null){
            throw new DataNotMatchHandler("존재하지 않는 관리자 ID");
        }
        if(!passwordEncoder.matches(adminLoginDto.getPassword(),admin.getAdminPwd())){
            throw new DataNotMatchHandler("비밀번호가 다릅니다");
        }

        AdminKey adminKey =new AdminKey();
        adminKey.setStatus("200");
        adminKey.setMessage("관리자 페이지 로그인 성공");
        adminKey.setData(admin.getAdminKey());

        return adminKey;
    }



    public AdminResultDto getCountRehab(String adminKey){
        checkAdmin(adminKey);
        long count=rehabilitatorRepository.countByStatus(false);
        AdminResultDto adminResultDto = new AdminResultDto();
        adminResultDto.setStatus("200");
        adminResultDto.setMessage("현재 "+String.valueOf(count)+"건의 재활사 대기승인이 있습니다");
        adminResultDto.setData(String.valueOf(count));
        return adminResultDto;
    }

    public RehabAllDto getRehabList(String adminKey){
        checkAdmin(adminKey);
        List<Rehabilitator> rehabilitators = rehabilitatorRepository.findAll();
        if(rehabilitators==null){
            throw new DataNotMatchHandler("재활사 인원이 존재하지 않습니다");
        }
        List<RehabListDto> rehabListDtoList = new ArrayList<>();
        for(Rehabilitator rehabilitator :rehabilitators){
            Member member = memberRepository.findByIdx(rehabilitator.getRefId());
            RehabListDto rehabListDto =new RehabListDto();
            rehabListDto.setMemberIdx(member.getIdx());
            rehabListDto.setProfile(member.getProfile());
            rehabListDto.setName(member.getName());
            rehabListDto.setCert(rehabilitator.getCert());
            rehabListDto.setDivision(rehabilitator.getDivision());
            rehabListDto.setDay(rehabilitator.getDay());
            rehabListDto.setPhoneNumber(member.getPhoneNumber());
            rehabListDto.setActivityArea(rehabilitator.getRegion());
            rehabListDto.setStatus(rehabilitator.getCheckEnum());
            rehabListDtoList.add(rehabListDto);
        }
        RehabAllDto rehabAllDto = new RehabAllDto();
        rehabAllDto.setStatus("200");
        rehabAllDto.setMessage("재활사 전체 목록 조회");
        rehabAllDto.setData(rehabListDtoList);
        return rehabAllDto;
    }

    public InfoDataDto getReInfo(String adminKey, Long memberIdx){
        checkAdmin(adminKey);
        Member member= memberRepository.findByIdx(memberIdx);
        Rehabilitator rehabilitator =rehabilitatorRepository.findByRefId(memberIdx);


        if(rehabilitator==null){
            throw new DataNotMatchHandler("존재하지 않는 재활사");
        }
        ReInfoDto reInfoDto=new ReInfoDto();
        reInfoDto.setMemberIdx(member.getIdx());
        reInfoDto.setSex(member.getSex());
        reInfoDto.setMemberId(member.getMemberId());
        reInfoDto.setName(member.getName());
        reInfoDto.setNickName(member.getNickName());
        reInfoDto.setEmail(member.getEmail());
        reInfoDto.setMemberType(member.getMemberType());
        reInfoDto.setPhoneNumber(member.getPhoneNumber());
        reInfoDto.setBirth(member.getBirth());
        reInfoDto.setAddress(member.getAddress());

        reInfoDto.setGuardian(member.getGuardian());
        reInfoDto.setPhone1(member.getPhone1());
        reInfoDto.setPhone2(member.getPhone2());
        reInfoDto.setParentsSex(member.getParentSex());
        reInfoDto.setParentsBirth(member.getParentBirth());
        reInfoDto.setMkService(member.getMkService());
        reInfoDto.setDetailAddress(rehabilitator.getDetailAddress());

        reInfoDto.setActivityArea(rehabilitator.getRegion());
        reInfoDto.setCert(rehabilitator.getCert());
        reInfoDto.setCareerDay(rehabilitator.getCareerDay());
        reInfoDto.setCareer(rehabilitator.getCareer());
        reInfoDto.setDivision(rehabilitator.getDivision());
        reInfoDto.setProfile(member.getProfile());
        reInfoDto.setIntro(member.getIntro());
        reInfoDto.setCheckEnum(rehabilitator.getCheckEnum());

        InfoDataDto infoDataDto = new InfoDataDto();
        infoDataDto.setStatus("200");
        infoDataDto.setMessage("재활사 정보");
        infoDataDto.setData(reInfoDto);
        return infoDataDto;
    }

    public AdminResultDto permitRehab(String adminKey, PermitDto permitDto){
        checkAdmin(adminKey);
        Member member=memberRepository.findByIdx(permitDto.getMemberIdx());
        if(member==null){
            throw new DataNotMatchHandler("존재하지 않는 재활사 입니다");
        }
        Rehabilitator rehabilitator = rehabilitatorRepository.findByRefId(member.getIdx());
        String r;
        Boolean check=rehabilitator.isStatus();
        if(permitDto.getCheckEnum().equals("01")){
            check=true;
        }else {
            check=false;
        }

        log.info("memberId: "+String.valueOf(permitDto.getMemberIdx())+"상태 :" +check);
        rehabilitator.setStatus(check);
        rehabilitator.setCheckEnum(permitDto.getCheckEnum());
        rehabilitatorRepository.save(rehabilitator);

        AdminResultDto adminResultDto = new AdminResultDto();
        adminResultDto.setStatus("200");
        adminResultDto.setMessage("재활사 상태변경 완료");
        adminResultDto.setData("재활사 상태변경 완료");
        return adminResultDto;
    }



    public ResultMemberListDto getMemberList(String adminKey){
        checkAdmin(adminKey);
        List<Member> allMember = memberRepository.findAll();
        if(allMember==null){
            throw new NoDataExceptionHandler("가입된 일반회원이 없습니다");
        }
        List<MemberListDto> memberListDtos = new ArrayList<>();
        for(Member member : allMember){
            if(!member.getMemberType().equals("02")){
                MemberListDto memberListDto =new MemberListDto();
                memberListDto.setMemberIdx(member.getIdx());
                memberListDto.setName(member.getName());
                memberListDto.setMemberType(member.getMemberType());
                memberListDto.setDay(member.getDay());
                memberListDto.setPhoneNumber(member.getPhoneNumber());
                memberListDto.setMorePn(member.getMorePn());
                memberListDto.setProfile(member.getProfile());
                memberListDto.setStatus(member.getLoginStatus());
                memberListDtos.add(memberListDto);
            }
        }
        ResultMemberListDto resultMemberListDto = new ResultMemberListDto();
        resultMemberListDto.setStatus("200");
        resultMemberListDto.setMessage("일반 회원 전체 목록 조회");
        resultMemberListDto.setData(memberListDtos);
        return resultMemberListDto;
    }

    public ResultMemberInfoDetailDto memberInfo(String adminKey, long memberIdx){
        checkAdmin(adminKey);
        Member member = memberRepository.findByIdx(memberIdx);
        if(member==null){
            throw new DataNotMatchHandler("삭제된 회원 입니다");
        }


        AMemberInfoDto aMemberInfoDto =new AMemberInfoDto();
        aMemberInfoDto.setMemberIdx(member.getIdx());
        aMemberInfoDto.setSex(member.getSex());
        aMemberInfoDto.setName(member.getName());
        aMemberInfoDto.setNickName(member.getNickName());
        aMemberInfoDto.setMemberId(member.getMemberId());
        aMemberInfoDto.setMemberType(member.getMemberType());
        aMemberInfoDto.setPhoneNumber(member.getPhoneNumber());
        aMemberInfoDto.setGuardian(member.getGuardian());
        aMemberInfoDto.setMorePn(member.getMorePn());
        aMemberInfoDto.setProfile(member.getProfile());
        aMemberInfoDto.setEmail(member.getEmail());
        aMemberInfoDto.setAddress(member.getAddress());
        aMemberInfoDto.setBirth(member.getBirth());

        aMemberInfoDto.setPhone1(member.getPhone1());
        aMemberInfoDto.setPhone2(member.getPhone2());
        aMemberInfoDto.setParentsSex(member.getParentSex());
        aMemberInfoDto.setParentsBirth(member.getParentBirth());
        aMemberInfoDto.setMemberShip(member.getMemberShip());
        aMemberInfoDto.setMkService(member.getMkService());

        ResultMemberInfoDetailDto resultMemberInfoDetailDto = new ResultMemberInfoDetailDto();
        resultMemberInfoDetailDto.setStatus("200");
        resultMemberInfoDetailDto.setMessage(member.getName()+"의 정보");
        resultMemberInfoDetailDto.setData(aMemberInfoDto);
        return resultMemberInfoDetailDto;
    }

    public ResultDto deleteUser(String adminKey, long memberIdx){
        checkAdmin(adminKey);
        Member member = memberRepository.findByIdx(memberIdx);
        if(member==null){
            throw new NoDataExceptionHandler("이미 삭제된 회원 입니다");
        }

        if(member.getMemberType().equals("02")){
            Rehabilitator rehabilitator = rehabilitatorRepository.findByRefId(member.getIdx());
            rehabilitatorRepository.delete(rehabilitator);
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
        resultDto.setMessage("회원 탈퇴 완료");
        resultDto.setData("회원 탈퇴 완료");
        return  resultDto;
    }

    public ResultDto updateReByAdmin(String adminKey, Long memberIdx,
                                     String birth,String address,String guardian,String phone1, String phone2,
                                     String parentsSex,String parentsBirth,Boolean mkService,String detailAddress,
                                     String activityArea,List<String> cert,List<String> careerDay,List<String> career,
                                     String division,String intro,MultipartFile profile,String checkEnum){
        checkAdmin(adminKey);

        Member member = memberRepository.findByIdx(memberIdx);
        if(member==null){
            throw new DataNotMatchHandler("없는 회원 입니다.");
        }

        if(member.getMemberType().equals("01")){
            throw new DataNotMatchHandler("일반 회원의 정보로 재활사의 정보에 접근 불가능합니다");
        }
        Rehabilitator rehabilitator = rehabilitatorRepository.findByRefId(member.getIdx());
        if(rehabilitator==null){
            throw new NoDataExceptionHandler("없는 재활사 입니다");
        }



        String getBirth;
        if(birth==null || birth.isEmpty()){
            getBirth=member.getBirth();
        }else{
            getBirth=birth;
        }

        String getAddress;
        if(address==null || address.isEmpty()){
            getAddress=member.getAddress();
        }else{
            getAddress=address;
        }

//        String getDetailAddress;
//        if(detailAddress==null || detailAddress.isEmpty()){
//            getDetailAddress=rehabilitator.getDetailAddress();
//        }else{
//            getDetailAddress=detailAddress;
//        }

        String getGuardian;
        if(guardian==null || guardian.isEmpty()){
            getGuardian=member.getGuardian();
        }else{
            getGuardian=guardian;
        }

        String getPhone1;
        if(phone1==null || phone1.isEmpty()){
            getPhone1=member.getPhone1();
        }else{
            getPhone1=phone1;
        }

        String getPhone2;
        if(phone2==null || phone2.isEmpty()){
            getPhone2=member.getPhone2();
        }else{
            getPhone2=phone2;
        }

        String getParentsSex;
        if(parentsSex==null || parentsSex.isEmpty()){
            getParentsSex=member.getParentSex();
        }else{
            getParentsSex=parentsSex;
        }

        String getParentsBirth;
        if(parentsBirth==null || parentsBirth.isEmpty()){
            getParentsBirth=member.getParentBirth();
        }else{
            getParentsBirth=parentsBirth;
        }

        Boolean getService;
        if(mkService==null){
            getService=member.getMkService();
        }else{
            getService=mkService;
        }

        String getDetailAddress;
        if(detailAddress==null || detailAddress.isEmpty()){
            getDetailAddress=rehabilitator.getDetailAddress();
        }else{
            getDetailAddress=detailAddress;
        }

        String getActivityArea;
        if(activityArea==null || activityArea.isEmpty()){
            getActivityArea=rehabilitator.getRegion();
        }else{
            getActivityArea=activityArea;
        }

        List<String> getCerts;
        if(cert==null || cert.isEmpty()){
            getCerts = rehabilitator.getCert();
        }else{
            getCerts=cert;
        }

        List<String> getCareer;
        if(career==null || career.isEmpty()){
            getCareer=rehabilitator.getCareer();
        }else{
            getCareer=career;
        }

        List<String> getCareerDay;
        if(careerDay==null || careerDay.isEmpty()){
            getCareerDay=rehabilitator.getCareerDay();
        }else{
            getCareerDay=careerDay;
        }

        String getDivision;
        if(division==null || division.isEmpty()){
            getDivision=rehabilitator.getDivision();
        }else{
            getDivision=division;
        }

        String getIntro;
        if(intro==null || intro.isEmpty()){
            getIntro=member.getIntro();
        }else{
            getIntro=intro;
        }

        String getProfile;
        if(profile==null || profile.isEmpty()){
            getProfile=member.getProfile();
        }else{
            String fileExtension = profile.getOriginalFilename().substring(profile.getOriginalFilename().lastIndexOf("."));

            // 오늘 날짜를 포함한 새로운 파일 이름 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date()) + fileExtension;
            getProfile=s3UploadService.uploadImage(profile,"member/"+member.getMemberKey()+"/"+newFileName);

        }

        String getCheckEnum;
        if(checkEnum==null || checkEnum.isEmpty()){
            getCheckEnum=rehabilitator.getCheckEnum();
        }else{
            getCheckEnum=checkEnum;
        }

        member.setBirth(getBirth);
        member.setAddress(getAddress);
        member.setGuardian(getGuardian);
        member.setPhone1(getPhone1);
        member.setPhone2(getPhone2);
        member.setParentSex(getParentsSex);
        member.setParentBirth(getParentsBirth);
        member.setMkService(getService);
        member.setIntro(getIntro);
        member.setProfile(getProfile);

        memberRepository.save(member);

        rehabilitator.setDetailAddress(getDetailAddress);
        rehabilitator.setRegion(getActivityArea);
        rehabilitator.setCert(getCerts);
        rehabilitator.setCareerDay(getCareerDay);
        rehabilitator.setCareer(getCareer);
        rehabilitator.setDivision(getDivision);
        rehabilitator.setCheckEnum(getCheckEnum);

        rehabilitatorRepository.save(rehabilitator);


        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("재활사 회원 변경 완료(관리자 권한)");
        resultDto.setData("재활사 회원 변경 완료(관리자 권한)");

        return resultDto;
    }

    public ResultDto updateMemberByAdmin(String adminKey, Long memberIdx, String sex, String name, String morePn,
                                         String guardian,String address,String birth,String phone1,String phone2,
                                         String parentsSex,String parentsBirth,String memberShip,Boolean mkService,
                                         MultipartFile profile){
        checkAdmin(adminKey);

        checkAdmin(adminKey);

        Member member = memberRepository.findByIdx(memberIdx);
        if(member==null){
            throw new DataNotMatchHandler("없는 회원 입니다.");
        }

        String getSex;
        if(sex==null || sex.isEmpty()){
            getSex=member.getSex();
        }else{
            getSex=sex;
        }

        String getName;
        if(name==null || name.isEmpty()){
            getName=member.getName();
        }else{
            getName=name;
        }

        String getMorePn;
        if(morePn==null || morePn.isEmpty()){
            getMorePn=member.getMorePn();
        }else {
            getMorePn=morePn;
        }

        String getGuardian;
        if(guardian==null || guardian.isEmpty()){
            getGuardian=member.getGuardian();
        }else{
            getGuardian=guardian;
        }

        String getAddress;
        if(address==null || address.isEmpty()){
            getAddress=member.getAddress();
        }else {
            getAddress=address;
        }

        String getBirth;
        if(birth==null || birth.isEmpty()){
            getBirth=member.getBirth();
        }else{
            getBirth=birth;
        }

        String getPhone1;
        if(phone1==null || phone1.isEmpty()){
            getPhone1=member.getPhone1();
        }else{
            getPhone1=phone1;
        }

        String getPhone2;
        if(phone2==null || phone2.isEmpty()){
            getPhone2=member.getPhone2();
        }else{
            getPhone2=phone2;
        }

        String getParentsSex;
        if(parentsSex==null || parentsSex.isEmpty()){
            getParentsSex=member.getParentSex();
        }else{
            getParentsSex=parentsSex;
        }

        String getParentsBirth;
        if(parentsBirth==null || parentsBirth.isEmpty()){
            getParentsBirth=member.getParentBirth();
        }else{
            getParentsBirth=parentsBirth;
        }

        String getMemberShip;
        if(memberShip==null || memberShip.isEmpty()){
            getMemberShip=member.getMemberShip();
        }else{
            getMemberShip=memberShip;
        }

        Boolean getService;
        if(mkService==null){
            getService=member.getMkService();
        }else{
            getService=mkService;
        }

        String getProfile;
        if(profile==null || profile.isEmpty()){
            getProfile=member.getProfile();
        }else{
            String fileExtension = profile.getOriginalFilename().substring(profile.getOriginalFilename().lastIndexOf("."));

            // 오늘 날짜를 포함한 새로운 파일 이름 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date()) + fileExtension;
            getProfile=s3UploadService.uploadImage(profile,"member/"+member.getMemberKey()+"/"+newFileName);

        }

        member.setSex(getSex);
        member.setName(getName);
        member.setMorePn(getMorePn);
        member.setGuardian(getGuardian);
        member.setAddress(getAddress);
        member.setBirth(getBirth);
        member.setPhone1(getPhone1);
        member.setPhone2(getPhone2);
        member.setParentSex(getParentsSex);
        member.setParentBirth(getParentsBirth);
        member.setMemberShip(getMemberShip);
        member.setMkService(getService);
        member.setProfile(getProfile);

        memberRepository.save(member);





        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("일반 회원 변경 완료(관리자 권한)");
        resultDto.setData("일반 회원 변경 완료(관리자 권한)");
        return resultDto;
    }

    public ResultDto updateProfile(String adminKey, Long memberIdx, MultipartFile newImage){
        checkAdmin(adminKey);
        Member member = memberRepository.findByIdx(memberIdx);
        if(member==null){
            throw new NoDataExceptionHandler("삭제된 회원입니다");
        }
        String getImage;
        String imageName;
        if(newImage.getSize()>30000000) {
            throw new DataNotMatchHandler("30MB 크기는 업로드 할 수 없습니다");
        }
        if(newImage.isEmpty()){
            member.setProfile("null");
            memberRepository.save(member);
        }else{
            String fileExtension = newImage.getOriginalFilename().substring(newImage.getOriginalFilename().lastIndexOf("."));

            // 오늘 날짜를 포함한 새로운 파일 이름 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date()) + fileExtension;
            getImage=s3UploadService.uploadImage(newImage,"member/"+member.getMemberKey()+"/"+newFileName);
            member.setProfile(getImage);
            memberRepository.save(member);
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("일반 회원 변경 완료(관리자 권한)");
        resultDto.setData("일반 회원 변경 완료(관리자 권한)");
        return resultDto;
    }

    public ResultDto ben(String adminKey, BenMemberDto benMemberDto){
        checkAdmin(adminKey);
        Member  member = memberRepository.findByIdx(benMemberDto.getMemberIdx());
        if(member==null){
            throw new NoDataExceptionHandler("없는 회원 입니다");
        }

        boolean s;
        String msg;
        if(member.getLoginStatus()==Boolean.TRUE){
            s=Boolean.FALSE;
            msg = "일반회원 권한 정지(관리자 권한)";
        }else {
            s=Boolean.TRUE;
            msg = "일반회원 권한 정지 해제(관리자 권한)";
        }

        member.setLoginStatus(s);
        memberRepository.save(member);
        log.info("회원 권한 변경: ");
        log.info(msg);
        log.info("회원 ID : " + member.getMemberId());


        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage(msg);
        resultDto.setData(msg);
        return resultDto;
    }

}