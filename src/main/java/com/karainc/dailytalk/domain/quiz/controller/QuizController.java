package com.karainc.dailytalk.domain.quiz.controller;

import com.karainc.dailytalk.domain.category.entitiy.Category;
import com.karainc.dailytalk.domain.quiz.controller.dto.requset.*;
import com.karainc.dailytalk.domain.quiz.controller.dto.response.*;
import com.karainc.dailytalk.domain.quiz.service.QuizService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;


    // 백오피스용 qna api

    // 카테고리 및 타이틀 만들기
    @PostMapping("/title")
    public QuizMakeResultDto create(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false) String adminKey,
                                    @RequestBody MakeDto makeDto){
        return quizService.create(makeDto,adminKey);
    }

    // 시험 문제 만들기
    @PostMapping("/new-quiz")
    public ResultDto createQna(@RequestParam(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                               @RequestParam(value = "quizIdx") Long quizIdx,
                               @RequestParam(value = "question") String question,
                               @RequestParam(value = "choices") List<String> choices,
                               @RequestParam(value = "answer") String answer,
                               @RequestParam(value = "image", required = false) MultipartFile image)
    {
        return quizService.createQuiz(adminKey,quizIdx,question,choices,answer,image);
    }

    @PostMapping("/set-level")
    public ResultDto setLevel(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                              @RequestBody LevelDto levelDto){
        return quizService.setting(adminKey,levelDto);
    }

    // 부분 수정
    @PutMapping("/update")
    public ResultDto changeQna(@RequestParam(value = "X-ADMIN-TOKEN") String adminKey,
                               @RequestParam(value ="quizIdx") Long quizIdx,
                               @RequestParam(value = "quizNumber") int quizNumber,
                               @RequestParam(value = "question") String question,
                               @RequestParam(value = "choices") List<String> choices,
                               @RequestParam(value = "answer") String answer,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               @RequestParam(value = "imageDelete") boolean imgDelete){
        return quizService.updateQuiz(adminKey,quizIdx,quizNumber,question,choices,answer,imgDelete,image);
    }

    @PutMapping("/update-title")
    public ResultDto changeTitle(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                 @RequestBody TitleDto titleDto){
        return quizService.changeTitleName(adminKey,titleDto);
    }

    @PostMapping("/title-image")
    public ResultDto changeImage(@RequestParam(value = "X-ADMIN-TOKEN")String adminKey,
                                 @RequestParam(value = "titleImage",required = false)MultipartFile titleImage,
                                 @RequestParam(value = "quizIdx")long quizIdx){
        return quizService.changeTitle(adminKey,titleImage,quizIdx);
    }

    @PutMapping("/update-category")
    public ResultDto changeCategory(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                    @RequestBody CategoryIdDto categoryIdDto){
        return quizService.changeCategory(adminKey,categoryIdDto);
    }

    // 부분 삭제
    @DeleteMapping("/delete-part")
    public ResultDto partDelete(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey,
                                @RequestParam Long quizIdx,
                                @RequestParam int quizNumber){

        return quizService.partDelete(adminKey,quizIdx,quizNumber);
    }

    // 모든 항목 퀴즈 조회
    @GetMapping("/test-qna-list")
    public QuizListDto getList(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey){
        return quizService.getAdminQuizList(adminKey);
    }

    // 카테 고리별 퀴즈 조회
//    @GetMapping("/category")
//    public QuizListDto getCategotyList(@RequestHeader("X-ADMIN-TOKEN")String adminKey,
//                                       @RequestParam String category){
//
//    }

    // 시험 문제 보기
    @GetMapping("/test-qna-info")
    public ResultQuizInfoDto getAdminQnaInfo(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                             @RequestParam long quizIdx){
        return quizService.getAdminQuizInfo(adminKey,quizIdx);
    }


    // 시험 결과 보기
    @PostMapping("/test-check")
    public ResultDto getTestCheck(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                  @RequestBody TestQuizDto testQuizDto){
        return quizService.testQuizCheck(adminKey,testQuizDto);
    }


    // 완전 삭제
    @DeleteMapping("/delete")
    public ResultDto deleteQuiz(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                @RequestParam long quizIdx){
        return quizService.deleteQuiz(adminKey,quizIdx);
    }

    // 문제 배포
    @PutMapping("/open-close")
    public ResultDto openQuiz(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                              @RequestBody OpenDto openDto){
        return quizService.openAndClose(adminKey,openDto);
    }

    // 어드민 전용 카테고리 조회
    @GetMapping("/category-for-admin")
    public CategoryListDto getQuizList(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                       @RequestParam long categoryIdx){
        return quizService.CategoryQuiz(adminKey,categoryIdx);
    }

    // 어드민 전용 응시인원 조회
    @GetMapping("/result-list")
    public DataListDto getResult(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                 @RequestParam Long categoryIdx){
        return quizService.dataList(adminKey,categoryIdx);
    }

    // 어드민 전용 해당 퀴즈 현황표
    @GetMapping("/now-list")
    public NowQList getNowList(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                               @RequestParam(value = "quizIdx",required = false)Long quizIdx){
        return quizService.getMemberQuizDashBoard(adminKey,quizIdx);
    }

    // 멤버용 퀴즈
    // 전체 조회
    @GetMapping("/qna-list")
    public QuizListDto getMemberList(){
        return quizService.getMemberQuizList();
    }

    // 카테고리 로 조회
    @GetMapping("/category-for-member")
    public CategoryListDto getMemberQuiz(@RequestParam long categoryIdx){
        return quizService.CategoryMemberQuiz(categoryIdx);
    }

    // 시험 보기
    @GetMapping("/info")
    public MemberResultQuizInfoDto getMemberQnaInfo(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                                    @RequestParam long quizIdx){
        return quizService.getMemberQuizInfo(memberKey,quizIdx);
    }


    // 체크
    @PostMapping("/check")
    public ResultDto getMemberCheck(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                  @RequestBody TestQuizDto testQuizDto){
        return quizService.MemberQuizCheck(memberKey,testQuizDto);
    }


    //memberKey 값을 기준으로 퀴즈 시험 결과 전부 조회 하기
    @GetMapping("/result-member")
    public ResultDataDto getMemberData(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return quizService.getMemberData(memberKey);
    }









}
