package com.karainc.dailytalk.domain.quiz.service;



import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.category.entitiy.Category;
import com.karainc.dailytalk.domain.category.repository.CategoryRepository;
import com.karainc.dailytalk.domain.contents.service.ContentService;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.DataRedundancyHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.member.service.MemberService;
import com.karainc.dailytalk.domain.quiz.controller.dto.data.*;
import com.karainc.dailytalk.domain.quiz.controller.dto.requset.*;
import com.karainc.dailytalk.domain.quiz.controller.dto.response.*;
import com.karainc.dailytalk.domain.quiz.entity.Quiz;
import com.karainc.dailytalk.domain.quiz.entity.QuizData;
import com.karainc.dailytalk.domain.quiz.repository.QuizDataRepository;
import com.karainc.dailytalk.domain.quiz.repository.QuizRepository;
import com.karainc.dailytalk.domain.utils.s3.service.S3UploadService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class QuizService {

    private final AdminService adminService;
    private final MemberService memberService;
    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;
    private final S3UploadService s3UploadService;
    private final QuizDataRepository quizDataRepository;
    private final CategoryRepository categoryRepository;
    private final ContentService contentService;

    // 어드민 전용 시험 서비스

    // 최초 1회 만들기
    public QuizMakeResultDto create(MakeDto makeDto , String adminKey){
        adminService.checkAdmin(adminKey);
        Category category= categoryRepository.findByCategoryId(makeDto.getCategoryIdx());
        if(category==null){
            throw new DataRedundancyHandler("카테고리를 먼저 만들어주세요");
        }


        List<String> newAnswers = new ArrayList<>();
        List<String> newQuestions = new ArrayList<>();
        List<List<String>> newChoices = new ArrayList<>();
        List<String> images = new ArrayList<>();

        Quiz quiz= Quiz.builder()
                .quizTitle(makeDto.getTitle())
                .categoryRef(makeDto.getCategoryIdx())
                .questions(newQuestions)
                .answers(newAnswers)
                .choices(newChoices)
                .images(images)
                .view(false)
                .build();

        Long nowQuizId = quizRepository.save(quiz).getQuizIdx();
        QuizMakeResultDto quizMakeResultDto =new QuizMakeResultDto();
        quizMakeResultDto.setStatus("200");
        quizMakeResultDto.setMessage("진단검사 초안 작성 완료");
        quizMakeResultDto.setData(nowQuizId);
        return quizMakeResultDto;
    }



    // 세부 퀴즈 만들기
    public ResultDto createQuiz(String adminKey,Long quizIdx,String question,List<String> choices,
                                String answer,MultipartFile image){
        adminService.checkAdmin(adminKey);
        Quiz quiz = quizRepository.findByQuizIdx(quizIdx);
        if(quiz==null) {
            throw new NoDataExceptionHandler("없는 퀴즈 입니다 먼저 카테고리와 제목을 작성해야합니다");
        }
        // 문제  답안지 정답 개수 점검
        if(question.isEmpty() || choices.isEmpty() || answer.isEmpty()){
            throw new DataNotMatchHandler("문제 작성오류 : 문제,답,보기 설정확인");
        }
        String imageName;
        if(image==null || image.isEmpty()){
            log.info("이미지 없음");
            imageName="null";
        }else if(image.getSize()>30000000) {
            throw new DataNotMatchHandler("최대 30MB 크기의 이미지만 업로드 가능합니다");
        } else{
            log.info("이미지 추가");
            String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));

            // 오늘 날짜를 포함한 새로운 파일 이름 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String newFileName = dateFormat.format(new Date()) + fileExtension;
            imageName=s3UploadService.uploadImage(image,"quiz/"+String.valueOf(quiz.getQuizIdx())+"/"+newFileName);
        }




        List<String> getQuestion = quiz.getQuestions();
        List<List<String>> getChoices = quiz.getChoices();
        List<String> getAnswers = quiz.getAnswers();
        List<String> getImages = quiz.getImages();

        getQuestion.add(question);
        getChoices.add(choices);
        getAnswers.add(answer);
        getImages.add(imageName);

        quiz.setQuestions(getQuestion);
        quiz.setChoices(getChoices);
        quiz.setAnswers(getAnswers);
        quiz.setImages(getImages);


        quizRepository.save(quiz);


        ResultDto resultDto =new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("문제 추가 완료");
        resultDto.setData("문제 추가 완료");
        return resultDto;
    }


    public ResultDto setting(String adminKey, LevelDto levelDto){
        adminService.checkAdmin(adminKey);
        Quiz quiz = quizRepository.findByQuizIdx(levelDto.getQuizIdx());
        if(quiz ==null){
            throw new NoDataExceptionHandler("없는 퀴즈 입니다");
        }
        log.info("퀴즈 레벨 설정 :"+levelDto.getQuizLevel() + levelDto.getQuizSubTitle()+levelDto.getQuizAge());
        String getST;
        String getA;
        String getL;
        if(levelDto.getQuizLevel()==null ||levelDto.getQuizLevel().isEmpty() ){
            getL=quiz.getQuizLevel();
        }else{
            getL=levelDto.getQuizLevel();
        }
        if(levelDto.getQuizAge()==null ||levelDto.getQuizAge().isEmpty() ){
            getA=quiz.getQuizAge();
        }else{
            getA=levelDto.getQuizAge();
        }
        if(levelDto.getQuizSubTitle()==null||levelDto.getQuizSubTitle().isEmpty()){
            getST=quiz.getQuizSubTitle();
        }else{
            getST=levelDto.getQuizSubTitle();
        }

        quiz.setQuizSubTitle(getST);
        quiz.setQuizLevel(getL);
        quiz.setQuizAge(getA);
        quizRepository.save(quiz);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("퀴즈 레벨 설정 완료(관리자 권한)");
        resultDto.setData("퀴즈 레벨 설정 완료(관리자 권한)");
        return resultDto;
    }

    // 퀴즈 정보 변경하기
    public ResultDto updateQuiz(String adminKey,Long quizIdx,int quizNumber,String question,List<String> choices,
                                String answer,boolean imageControl,MultipartFile image){
        adminService.checkAdmin(adminKey);
        Quiz getQuiz=quizRepository.findByQuizIdx(quizIdx);
        if(getQuiz==null){
            throw new DataNotMatchHandler("없는 퀴즈ID 입니다");
        }


        log.info("이미지 검사");
        String imageName;
        if(imageControl==false){
            if(image==null || image.isEmpty() || image.equals("null")){
                log.info("이미지 유지");
                imageName=getQuiz.getImages().get(quizNumber);
            }else{
                if(image.getSize()>30000000){
                    throw new DataNotMatchHandler("용량 초과 : 30MB 까지만 이미지 업로드가 가능합니다");
                }
                log.info("이미지 변경");
                String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));

                // 오늘 날짜를 포함한 새로운 파일 이름 생성
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String newFileName = dateFormat.format(new Date()) + fileExtension;
                imageName=s3UploadService.uploadImage(image,"quiz/"+String.valueOf(getQuiz.getQuizIdx())+"/"+newFileName);
            }
        }else {
            log.info("이미지 삭제");
            imageName="null";
        }

        List<String> getQuestions = getQuiz.getQuestions();
        List<List<String>> getChoices = getQuiz.getChoices();
        List<String> getAnswers = getQuiz.getAnswers();
        List<String> getImages = getQuiz.getImages();

        getQuestions.set(quizNumber,question);
        getChoices.set(quizNumber, choices);
        getAnswers.set(quizNumber,answer);
        if(getImages.size()<=0){
            getImages.add(imageName);
        }else{
            getImages.set(quizNumber,imageName);
        }
        getQuiz.setQuestions(getQuestions);
        getQuiz.setAnswers(getAnswers);
        getQuiz.setChoices(getChoices);
        getQuiz.setImages(getImages);
        quizRepository.save(getQuiz);

        ResultDto resultDto =new ResultDto();
        resultDto.setStatus("200");
        resultDto.setData("문제 수정 완료");
        resultDto.setMessage("문제 수정 완료");
        return resultDto;
    }

    public QuizListDto getAdminQuizList(String adminKey){
        adminService.checkAdmin(adminKey);
        List<Quiz> allQuiz = quizRepository.findAll();
        if(allQuiz.size()==0 || allQuiz==null){
            throw new NoDataExceptionHandler("작성된 퀴즈 없음");
        }
        List<QuizDto> quizDtos = new ArrayList<>();
        for(Quiz getQuiz : allQuiz){
            Category category =categoryRepository.findByCategoryId(getQuiz.getCategoryRef());
            long ctIdx;
            String ctName;
            if(category==null){
                ctIdx=0;
                ctName="빈 카테고리";
            }else{
                ctIdx=category.getCategoryId();
                ctName=category.getCategoryName();
            }
            QuizDto quizDto= new QuizDto();
            quizDto.setQuizIdx(getQuiz.getQuizIdx());
            quizDto.setCategoryIdx(ctIdx);
            quizDto.setCategory(ctName);
            quizDto.setQuizTitle(getQuiz.getQuizTitle());
            quizDto.setTitleImage(getQuiz.getTitleImage());
            quizDto.setQCount(getQuiz.getQuestions().size());
            quizDto.setView(getQuiz.getView());
            quizDtos.add(quizDto);
        }

        QuizListDto quizListDto = new QuizListDto();
        quizListDto.setStatus("200");
        quizListDto.setMessage("퀴즈목록 전체조회(관리자용)");
        quizListDto.setData(quizDtos);
        return quizListDto;
    }




    public ResultQuizInfoDto getAdminQuizInfo(String adminKey,long quizId){
        adminService.checkAdmin(adminKey);
        Quiz quiz = quizRepository.findByQuizIdx(quizId);
        if(quiz == null) {
            throw new DataNotMatchHandler("삭제된 퀴즈 입니다");
        }
        long ctIdx;
        String ctName;
        Category category = categoryRepository.findByCategoryId(quiz.getCategoryRef());
        if(category==null){
            ctIdx=0;
            ctName="카테고리 없음";
        }else {
            ctIdx=category.getCategoryId();
            ctName=category.getCategoryName();
        }
        QuizInfoDto quizInfoDto = new QuizInfoDto();
        List<QuizInfoDetailDto> quizInfoDetailDtos = new ArrayList<>();
        quizInfoDto.setQuizIdx(quiz.getQuizIdx());
        quizInfoDto.setCategoryIdx(ctIdx);
        quizInfoDto.setCategory(ctName);
        quizInfoDto.setQuizTitle(quiz.getQuizTitle());
        quizInfoDto.setQuizAge(quiz.getQuizAge());
        quizInfoDto.setQuizSubTitle(quiz.getQuizSubTitle());
        quizInfoDto.setQuizLevel(quiz.getQuizLevel());
        quizInfoDto.setTitleImage(quiz.getTitleImage());

        for(int i=0; i<quiz.getQuestions().size(); i++){
            String image;
            QuizInfoDetailDto quizInfoDetailDto = new QuizInfoDetailDto();
            quizInfoDetailDto.setQuestion(quiz.getQuestions().get(i));
            quizInfoDetailDto.setChoices(quiz.getChoices().get(i));
            quizInfoDetailDto.setAnswer(quiz.getAnswers().get(i));
            if(quiz.getImages().get(i).equals("null") ||quiz.getImages().get(i)==null){
                image = "null";
            }else {
                image = quiz.getImages().get(i);
            }
            quizInfoDetailDto.setImage(image);
            quizInfoDetailDtos.add(quizInfoDetailDto);
        }

        quizInfoDto.setQna(quizInfoDetailDtos);

        ResultQuizInfoDto resultQuizInfoDto =new ResultQuizInfoDto();
        resultQuizInfoDto.setStatus("200");
        resultQuizInfoDto.setMessage(String.valueOf(quizId)+"의 정보");
        resultQuizInfoDto.setData(quizInfoDto);
        return resultQuizInfoDto;
    }


    // 문제 일부분 지우기
    public ResultDto partDelete(String adminKey, Long quizIdx, int quizNumber){
        adminService.checkAdmin(adminKey);

        Quiz quiz = quizRepository.findByQuizIdx(quizIdx);
        if(quiz==null){
            throw new NoDataExceptionHandler("이미 지워진 데이터 입니다");
        }

        List<String> getQuestions = quiz.getQuestions();
        List<List<String>> getChoices = quiz.getChoices();
        List<String> getAnswers = quiz.getAnswers();
        List<String> getImages = quiz.getImages();

        getQuestions.remove(quizNumber);
        getChoices.remove(quizNumber);
        getAnswers.remove(quizNumber);
        getImages.remove(quizNumber);

        quiz.setQuestions(getQuestions);
        quiz.setChoices(getChoices);
        quiz.setAnswers(getAnswers);
        quiz.setImages(getImages);

        quizRepository.save(quiz);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("부분 삭제 완료");
        resultDto.setData("부분 삭제 완료");
        return resultDto;
    }

    public ResultDto testQuizCheck(String adminKey, TestQuizDto testQuizDto){
        adminService.checkAdmin(adminKey);
        Quiz quiz = quizRepository.findByQuizIdx(testQuizDto.getQuizIdx());
        if(quiz==null){
            throw new DataNotMatchHandler("현재 퀴즈는 삭제 되었습니다");
        }
        if(quiz.getAnswers().size() != testQuizDto.getAnswers().size()){
            throw new DataNotMatchHandler("풀지 않은 문제를 확인해주세요");
        }
        int point =0;
        for(int i=0; i<testQuizDto.getAnswers().size(); i++){
            if(testQuizDto.getAnswers().get(i).equals(quiz.getAnswers().get(i))){
                System.out.println(i+"번째 정답비교: " +testQuizDto.getAnswers().get(i) + "" +quiz.getAnswers().get(i));
                point++;
            }
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("테스트(관리자) 점수: " + String.valueOf(point)+"입니다(통계 안잡힘)");
        resultDto.setData("테스트(관리자) 점수: " + String.valueOf(point)+"입니다(통계 안잡힘)");
        return resultDto;
    }

    // 퀴즈 현황표
    public NowQList getMemberQuizDashBoard(String adminKey,Long quizIdx){
        adminService.checkAdmin(adminKey);

        List<QuizData> quizDataList = quizDataRepository.findByQuizRef(quizIdx);
        int k = quizDataList.size();

        if(k==0){
            throw new NoDataExceptionHandler("퀴즈 현황표가 없습니다");
        }

        List<NowMemberQuizDto> nowMemberQuizDtoList = new ArrayList<>();
        for(QuizData quizData :quizDataList){
            NowMemberQuizDto nowMemberQuizDto = new NowMemberQuizDto();
            Member member = memberRepository.findByIdx(quizData.getMRef());
            if(member!=null){
                nowMemberQuizDto.setMemberId(member.getMemberId());
                nowMemberQuizDto.setName(member.getName());
                nowMemberQuizDto.setProfile(member.getProfile());

                nowMemberQuizDto.setPoint(quizData.getCorrectRate()==null ? 0 :(int) Double.parseDouble(quizData.getCorrectRate()));
                nowMemberQuizDto.setCorrectRate(quizData.getCorrectRate()==null ? "0" :quizData.getCorrectRate());
                nowMemberQuizDto.setIncorrectRate(quizData.getIncorrectRate()==null ? "0": quizData.getIncorrectRate());
                nowMemberQuizDto.setTestDay(quizData.getTestDay());
                nowMemberQuizDto.setTestTime(quizData.getTestTime() == null ? 0 : quizData.getTestTime());
                nowMemberQuizDtoList.add(nowMemberQuizDto);
            }
        }
        Collections.reverse(nowMemberQuizDtoList);

        NowQList nowQList = new NowQList();
        nowQList.setStatus("200");
        nowQList.setMessage("퀴즈 현황표");
        nowQList.setData(nowMemberQuizDtoList);
        return nowQList;
    }


    // 퀴즈 전부 지우기
    public ResultDto deleteQuiz(String adminKey, long quizId){
        adminService.checkAdmin(adminKey);
        Quiz quiz = quizRepository.findByQuizIdx(quizId);
        if(quiz==null){
            throw new NoDataExceptionHandler("이미 지워진 퀴즈입니다");
        }
        s3UploadService.deleteFolder("quiz/"+String.valueOf(quiz.getQuizIdx()));
        quizRepository.delete(quiz);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("관리자 권한으로 삭제 완료");
        resultDto.setData("관리자 권한으로 삭제 완료");
        return resultDto;
    }

    public ResultDto changeCategory(String adminKey, CategoryIdDto categoryIdDto){
        adminService.checkAdmin(adminKey);
        Category category = categoryRepository.findByCategoryId(categoryIdDto.getCategoryIdx());
        if(category==null){
            throw new NoDataExceptionHandler("삭제된 카테고리 입니다");
        }
        Quiz quiz = quizRepository.findByQuizIdx(categoryIdDto.getQuizIdx());
        if(quiz==null){
            throw new NoDataExceptionHandler("삭제된 퀴즈 입니다");
        }

        quiz.setCategoryRef(categoryIdDto.getCategoryIdx());
        quizRepository.save(quiz);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("카테고리 수정 완료");
        resultDto.setData("카테고리 수정 완료");
        return resultDto;
    }


    // 유저용 서비스
    public QuizListDto getMemberQuizList(){
        List<Quiz> allQuiz = quizRepository.findByView(true);
        if(allQuiz.size()==0 || allQuiz==null){
            throw new NoDataExceptionHandler("작성된 퀴즈 없음");
        }
        List<QuizDto> quizDtos = new ArrayList<>();
        for(Quiz getQuiz : allQuiz){
            Category category =categoryRepository.findByCategoryId(getQuiz.getCategoryRef());
            if(category !=null){
                QuizDto quizDto= new QuizDto();
                quizDto.setQuizIdx(getQuiz.getQuizIdx());
                quizDto.setQuizTitle(getQuiz.getQuizTitle());
                quizDto.setTitleImage(getQuiz.getTitleImage());
                quizDto.setQuizAge(getQuiz.getQuizAge());
                quizDto.setQuizLevel(getQuiz.getQuizLevel());
                quizDto.setQuizSubTitle(getQuiz.getQuizSubTitle());
                quizDto.setCategoryIdx(category.getCategoryId());
                quizDto.setCategory(category.getCategoryName());
                quizDto.setView(getQuiz.getView());
                quizDto.setQCount(getQuiz.getQuestions().size());
                quizDtos.add(quizDto);
            }
        }
        QuizListDto quizListDto = new QuizListDto();
        quizListDto.setStatus("200");
        quizListDto.setMessage("퀴즈목록 전체조회");
        quizListDto.setData(quizDtos);
        return quizListDto;
    }
    public ResultDto openAndClose(String adminKey, OpenDto openDto){
        adminService.checkAdmin(adminKey);
        Quiz quiz = quizRepository.findByQuizIdx(openDto.getQuizIdx());
        if(quiz==null){
            throw new NoDataExceptionHandler("삭제된 퀴즈");
        }
        Boolean status;
        String message;
        if(quiz.getView()==false){
            status=true;
            message="문제 배포 완료";
        }else{
            status=false;
            message="문제 배포 중지";
        }
        quiz.setView(status);
        quizRepository.save(quiz);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setData(message);
        resultDto.setMessage(message);
        return resultDto;

    }


    public CategoryListDto CategoryQuiz(String adminKey,Long categoryIdx){
        adminService.checkAdmin(adminKey);
        log.info("관리자 권한 실행 : 카테고리별 퀴즈 조회");
        Category category = categoryRepository.findByCategoryId(categoryIdx);
        if(category==null){
            throw new NoDataExceptionHandler("없어진 카테고리 입니다");
        }
        List<Quiz> quizList = quizRepository.findByCategoryRef(categoryIdx);
        if(quizList.size()==0 || quizList==null){
            throw new NoDataExceptionHandler("퀴즈가 없습니다 퀴즈를 먼저 생성해주세요");
        }
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for(Quiz ctQuiz : quizList){
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryIdx(categoryIdx);
            categoryDto.setQuizIdx(ctQuiz.getQuizIdx());
            categoryDto.setQuizTitle(ctQuiz.getQuizTitle());
            categoryDto.setQuizSubTitle(ctQuiz.getQuizSubTitle());
            categoryDto.setQuizAge(ctQuiz.getQuizAge());
            categoryDto.setQuizLevel(ctQuiz.getQuizLevel());
            categoryDto.setQCount(ctQuiz.getQuestions().size());
            categoryDto.setView(ctQuiz.getView());
            categoryDtos.add(categoryDto);
        }

        CategoryListDto categoryListDto= new CategoryListDto();
        categoryListDto.setStatus("200");
        categoryListDto.setMessage("카테고리 기준 퀴즈 목록조회(관리자 권한)");
        categoryListDto.setData(categoryDtos);
        return categoryListDto;
    }

    public CategoryListDto CategoryMemberQuiz(Long categoryIdx){
        log.info("결제 회원 권한 실행 : 카테고리별 퀴즈 조회");
        Category category = categoryRepository.findByCategoryId(categoryIdx);
        if(category==null){
            throw new NoDataExceptionHandler("없어진 카테고리 입니다");
        }
        List<Quiz> quizList = quizRepository.findByCategoryRef(categoryIdx);
        if(quizList.size()==0 || quizList==null){
            throw new NoDataExceptionHandler("퀴즈가 없습니다 퀴즈를 먼저 생성해주세요");
        }
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for(Quiz ctQuiz : quizList){
            if(ctQuiz.getView()==Boolean.TRUE){
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setCategoryIdx(categoryIdx);
                categoryDto.setQuizIdx(ctQuiz.getQuizIdx());
                categoryDto.setQuizTitle(ctQuiz.getQuizTitle());
                categoryDto.setQuizSubTitle(ctQuiz.getQuizSubTitle());
                categoryDto.setQuizLevel(ctQuiz.getQuizLevel());
                categoryDto.setQuizAge(ctQuiz.getQuizAge());
                categoryDto.setTitleImage(ctQuiz.getTitleImage());
                categoryDto.setQCount(ctQuiz.getQuestions().size());
                categoryDto.setView(ctQuiz.getView());
                categoryDtos.add(categoryDto);
            }
        }

        CategoryListDto categoryListDto= new CategoryListDto();
        categoryListDto.setStatus("200");
        categoryListDto.setMessage("카테고리 기준 퀴즈 목록조회");
        categoryListDto.setData(categoryDtos);
        return categoryListDto;
    }

    public MemberResultQuizInfoDto getMemberQuizInfo(String memberKey, long quizId) {
        memberService.checkMember(memberKey);

        Member member = memberRepository.findByMemberKey(memberKey);
        contentService.findSubMember(member);

        Quiz quiz = quizRepository.findByQuizIdx(quizId);

        if (quiz == null) {
            throw new DataNotMatchHandler("삭제된 퀴즈 입니다");
        }


        Category category=categoryRepository.findByCategoryId(quiz.getCategoryRef());
        if(category==null || quiz.getView()==false){
            throw new DataNotMatchHandler("현재 퀴즈 수정중입니다 나중에 사용가능합니다");
        }

        MemberQuizInfoDto memberQuizInfoDto = new MemberQuizInfoDto();
        List<MemberQuizInfoDetailDto> memberQuizInfoDetailDtos = new ArrayList<>();
        memberQuizInfoDto.setQuizId(quiz.getQuizIdx());
        memberQuizInfoDto.setCategoryIdx(category.getCategoryId());
        memberQuizInfoDto.setCategory(category.getCategoryName());
        memberQuizInfoDto.setQuizTitle(quiz.getQuizTitle());
        memberQuizInfoDto.setQuizSubTitle(quiz.getQuizSubTitle());
        memberQuizInfoDto.setQuizAge(quiz.getQuizAge());
        memberQuizInfoDto.setQuizLevel(quiz.getQuizLevel());
        memberQuizInfoDto.setTitleImage(quiz.getTitleImage());

        for(int i=0; i< quiz.getQuestions().size(); i++){
            MemberQuizInfoDetailDto memberQuizInfoDetailDto = new MemberQuizInfoDetailDto();
            memberQuizInfoDetailDto.setQuestion(quiz.getQuestions().get(i));
            memberQuizInfoDetailDto.setChoices(quiz.getChoices().get(i));
            memberQuizInfoDetailDto.setImage(quiz.getImages().get(i));
            memberQuizInfoDetailDtos.add(memberQuizInfoDetailDto);
        }

        memberQuizInfoDto.setQna(memberQuizInfoDetailDtos);

        MemberResultQuizInfoDto memberResultQuizInfoDto = new MemberResultQuizInfoDto();
        memberResultQuizInfoDto.setStatus("200");
        memberResultQuizInfoDto.setMessage(String.valueOf(quizId) + "의 정보");
        memberResultQuizInfoDto.setData(memberQuizInfoDto);

        return memberResultQuizInfoDto;
    }

    public ResultDto MemberQuizCheck(String memberKey, TestQuizDto testQuizDto){
        memberService.checkMember(memberKey);

        Member member = memberRepository.findByMemberKey(memberKey);
        contentService.findSubMember(member);

        Quiz quiz = quizRepository.findByQuizIdx(testQuizDto.getQuizIdx());
        if(quiz==null){
            throw new DataNotMatchHandler("현재 퀴즈는 삭제 되었습니다");
        }
        if(quiz.getAnswers().size() != testQuizDto.getAnswers().size()){
            throw new DataNotMatchHandler("풀지 않은 문제를 확인해주세요");
        }
        Category category = categoryRepository.findByCategoryId(quiz.getCategoryRef());
        if(category==null || quiz.getView()==false){
            throw new NoDataExceptionHandler("문제 수정중입니다 수정된 시험으로 재응시 해야합니다");
        }


        int quizSize = quiz.getAnswers().size();

        int point =0;
        int inCorrect=0;
        for(int i=0; i<testQuizDto.getAnswers().size(); i++){
            if(testQuizDto.getAnswers().get(i).equals(quiz.getAnswers().get(i))){
                System.out.println(i+"번째 정답비교: " +testQuizDto.getAnswers().get(i) + "" +quiz.getAnswers().get(i));
                point++;
            }else{
                inCorrect ++;
            }
        }



        double accuracy = ((double) point / quizSize) * 100;
        double incorrectPercentage = ((double) inCorrect / quizSize) * 100;

        DecimalFormat df = new DecimalFormat("#.##");




        QuizData quizData = QuizData.builder()
                .quizCateGory(quiz.getCategoryRef())
                .quizRef(quiz.getQuizIdx())
                .mRef(member.getIdx())
                .quizCateGory(category.getCategoryId())
                .quizPoint(point)
                .correctRate(df.format(accuracy))
                .incorrectRate(df.format(incorrectPercentage))
                .testDay(LocalDate.now().toString())
                .testTime(testQuizDto.getTestTime())
                .build();
        quizDataRepository.save(quizData);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage(member.getNickName() +"의 점수는 " + String.valueOf(point)+"입니다");
        resultDto.setData(String.valueOf(point));
        return resultDto;
    }

    public DataListDto dataList(String adminKey,Long categoryIdx){
        adminService.checkAdmin(adminKey);
        Category category = categoryRepository.findByCategoryId(categoryIdx);
        if(category==null){
            throw new NoDataExceptionHandler("삭제된 카테고리는 반영하지 않습니다 카테고리를 수정해주세요");
        }
        List<QuizData> quizDataList = quizDataRepository.findByQuizCateGory(categoryIdx);
        if(quizDataList.size()<=0){
            throw new NoDataExceptionHandler("시험 결과 리스트가 없습니다");
        }
        List<QuizDataDto> quizDataDtoList = new ArrayList<>();
        for(QuizData quizData : quizDataList){
            Member member = memberRepository.findByIdx(quizData.getMRef());
            Quiz quiz = quizRepository.findByQuizIdx(quizData.getQuizRef());
            // 삭제된 회원은 제외해서 보여주기
            if(member !=null){
                QuizDataDto quizDataDto = new QuizDataDto();
                quizDataDto.setQuizDataIdx(quizData.getQuizDataIdx());
                quizDataDto.setProfile(member.getProfile());
                quizDataDto.setMemberIdx(member.getIdx());
                quizDataDto.setName(member.getName());
                quizDataDto.setQuizTitle(quiz.getQuizTitle());
                quizDataDto.setPoint(Integer.parseInt(quizData.getCorrectRate()));
                quizDataDto.setDate(quizData.getTestDay());
                quizDataDtoList.add(quizDataDto);
            }
        }
        DataListDto dataListDto = new DataListDto();
        dataListDto.setStatus("200");
        dataListDto.setMessage("카테고리별 퀴즈 결과 목록");
        dataListDto.setData(quizDataDtoList);

        return dataListDto;
    }

    public ResultDataDto getMemberData(String memberKey){
        log.info("개인 회원 퀴즈 목록 조회");
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);
        if(member == null){
            throw new NoDataExceptionHandler("삭제된 회원 입니다");
        }
        List<MemberDataDto> memberDataDtos = new ArrayList<>();
        List<QuizData> quizData = quizDataRepository.findBymRef(member.getIdx());
        if(quizData.size()==0 || quizData ==null){
            throw new NoDataExceptionHandler("응시 이력이 없습니다");
        }
        for(QuizData quizDataList : quizData){
            MemberDataDto memberDataDto = new MemberDataDto();
            Quiz quiz = quizRepository.findByQuizIdx(quizDataList.getQuizRef());
            Category category =categoryRepository.findByCategoryId(quizDataList.getQuizCateGory());
            if(quiz !=null && category !=null){
                memberDataDto.setCategoryIdx(category.getCategoryId());
                memberDataDto.setCategory(category.getCategoryName());
                memberDataDto.setQuizIdx(quiz.getQuizIdx());
                memberDataDto.setQuizTitle(quiz.getQuizTitle());
                memberDataDto.setPoint(quizDataList.getCorrectRate()==null ? 0 :(int) Double.parseDouble(quizDataList.getCorrectRate()));
                memberDataDto.setCorrectRate(quizDataList.getCorrectRate()==null ? "0" :quizDataList.getCorrectRate());
                memberDataDto.setIncorrectRate(quizDataList.getIncorrectRate()==null ? "0": quizDataList.getIncorrectRate());
                memberDataDto.setTestDay(quizDataList.getTestDay());
                System.out.println(quizDataList.getTestTime());
                memberDataDto.setTestTime(quizDataList.getTestTime() == null ? 0 : quizDataList.getTestTime());
                memberDataDtos.add(memberDataDto);
            }
        }
        Collections.reverse(memberDataDtos);
        ResultDataDto resultDataDto = new ResultDataDto();
        resultDataDto.setStatus("200");
        resultDataDto.setMessage("회원 퀴즈 결과 조회 : 삭제된 퀴즈나 카테고리는 반영되지 않습니다");
        resultDataDto.setData(memberDataDtos);
        return resultDataDto;
    }

    public ResultDto changeTitle (String adminKey, MultipartFile titleImage,long quizIdx){
        adminService.checkAdmin(adminKey);
        Quiz quiz = quizRepository.findByQuizIdx(quizIdx);
        String titleImageName;

        if(quiz==null){
            throw new NoDataExceptionHandler("없는 퀴즈 번호 입니다");
        }

        if(titleImage==null || titleImage.isEmpty()){
            titleImageName="null";
        }else{
            if(titleImage.getSize()>30000000){
                throw new DataNotMatchHandler("업로드 용량 초과: 최대 30MB 까지 가능 합니다");
            }else {
                String fileExtension = titleImage.getOriginalFilename().substring(titleImage.getOriginalFilename().lastIndexOf("."));

                // 오늘 날짜를 포함한 새로운 파일 이름 생성
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String newFileName = "title"+dateFormat.format(new Date()) + fileExtension;
                titleImageName=s3UploadService.uploadImage(titleImage,"quiz/"+String.valueOf(quiz.getQuizIdx())+"/"+newFileName);
            }
        }
        quiz.setTitleImage(titleImageName);
        quizRepository.save(quiz);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("퀴즈 대문 이미지 생성(관리자 권한)");
        resultDto.setData("퀴즈 대문 이미지 생성(관리자 권한)");
        return resultDto;
    }

    public ResultDto changeTitleName(String adminKey, TitleDto titleDto){
        log.info("타이틀 수정");
        adminService.checkAdmin(adminKey);
        Quiz quiz = quizRepository.findByQuizIdx(titleDto.getQuizIdx());
        if(quiz == null){
            throw new NoDataExceptionHandler("없는 퀴즈 입니다");
        }
        quiz.setQuizTitle(titleDto.getQuizTitle());
        quizRepository.save(quiz);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("퀴즈 타이틀 변경 (관리자 권한)");
        resultDto.setData("퀴즈 타이틀 변경 (관리자 권한)");

        return resultDto;
    }


}
