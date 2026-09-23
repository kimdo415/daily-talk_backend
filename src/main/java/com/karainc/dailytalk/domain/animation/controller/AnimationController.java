package com.karainc.dailytalk.domain.animation.controller;


import com.karainc.dailytalk.domain.animation.controller.dto.requset.*;
import com.karainc.dailytalk.domain.animation.controller.dto.response.*;
import com.karainc.dailytalk.domain.animation.service.AniCategoryService;
import com.karainc.dailytalk.domain.animation.service.AnimationService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/ani")
@RequiredArgsConstructor
public class AnimationController {

    private final AniCategoryService aniCategoryService;
    private final AnimationService animationService;


    // -- 어드민 권한 -- //

    // 카테고리 생성
    @PostMapping("/category/make")
    public AniCtResultDto ctMake(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                 @RequestBody AniCtDto aniCtDto){
        return aniCategoryService.make(adminKey,aniCtDto);
    }

    // 카테고리 업데이트
    @PutMapping("/category/update")
    public AniCtResultDto ctUpdate(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                   @RequestBody UpdateAniCtDto updateAniCtDto){
        return aniCategoryService.update(adminKey,updateAniCtDto);
    }

    // 카테고리 리스트
    @GetMapping("/category/list")
    public AniCategoryListDto getList(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey){
        return aniCategoryService.ctList(adminKey);
    }

    // 카테고리 삭제
    @DeleteMapping("/category/out")
    public AniCtResultDto tryDelete(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                                    @RequestParam long aniCategoryIdx){
        return aniCategoryService.ctDelete(adminKey,aniCategoryIdx);
    }

    // 회원단에서 카테고리 리스트 조회하기
    @GetMapping("/category/for-member")
    public AniCategoryListDto memberCtList(){
        return aniCategoryService.getList();
    }

    // 애니 초안 작성
    @PostMapping("/new-ani")
    public MakeResultDto firstAni(@RequestHeader(value = "X-ADMIN-TOKEN", required = false)String adminKey,
                                @RequestBody MakeDto makeDto){
        return animationService.makeAni(adminKey,makeDto);
    }

    //한문제씩 작성
    @PostMapping("/one-ani")
    public ResultDto oneAni(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                            @RequestBody AnimakeDto animakeDto){
        return animationService.newAni(adminKey,animakeDto);
    }

    //서브타이틀 , 레벨 , 나이 설정
    @PostMapping("/set-ani")
    public ResultDto setAni(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                            @RequestBody AniSetDto aniSetDto){
        return animationService.setAni(adminKey,aniSetDto);
    }

    //애니 부분 수정
    @PutMapping("/update-ani")
    public ResultDto change(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                            @RequestBody AniUpdateDto aniUpdateDto){
        return animationService.updateAni(adminKey,aniUpdateDto);
    }


    //애니 부분 삭제
    @DeleteMapping("/part-delete-ani")
    public ResultDto partOut(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                             @RequestParam long aniIdx,
                             @RequestParam int aniNumber){
        return animationService.partAniDelete(adminKey,aniIdx,aniNumber);
    }

    //애니 완전 삭제
    @DeleteMapping("/delete-ani")
    public ResultDto out(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                         @RequestParam long aniIdx){
        return animationService.deleteAni(adminKey,aniIdx);
    }

    //애니 타이틀 제목 변경
    @PutMapping("/change-title")
    public ResultDto changeTitle(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                 @RequestBody AniTitleDto aniTitleDto){
        return animationService.changeTitleName(adminKey,aniTitleDto);
    }

    //애니 타이틀 이미지 변경
    @PutMapping("/change-image")
    public ResultDto changeName(@RequestParam(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                @RequestParam(value = "aniIdx")long aniIdx,
                                @RequestParam(value = "image")MultipartFile image){
        return animationService.changeTitleImage(adminKey,aniIdx,image);
    }


    //애니 리스트 조회
    @GetMapping("/list")
    public AniInfoListDto getAllList(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return animationService.getAniList(adminKey);
    }

    //애니 상세조회
    @GetMapping("/info-admin")
    public ResultAniInfo getInfoAdmin(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                      @RequestParam long aniIdx){
        return animationService.getForAdmin(adminKey,aniIdx);
    }

    //애니 카테고리 변경
    @PutMapping("/change-category")
    public ResultDto inputCt(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                             @RequestBody InputCtDto inputCtDto){
        return animationService.input(adminKey,inputCtDto);
    }

    //애니 현황표
    @GetMapping("/now-list")
    public NowAniDto getNowAni(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                               @RequestParam(value = "aniIdx",required = false)Long aniIdx){
        return animationService.getNowAniDashBoard(adminKey,aniIdx);
    }


    //애니 출력 설정
    @PutMapping("/open-close")
    public ResultDto release(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                             @RequestBody OpenAndCloseDto openAndCloseDto){

        return animationService.openClose(adminKey,openAndCloseDto);
    }

    //애니 카테고리 조회
    @GetMapping("/category-admin")
    public CtAniListDto getAdminCt(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                   @RequestParam long aniCategoryIdx){

        return animationService.getCategoryAdmin(adminKey,aniCategoryIdx);
    }


    // -- 회원 및 일반 서비스 -- //


    // 누구든지 조회 애니 리스트조회
    @GetMapping("/list-all")
    public AniInfoListDto getAll(){
        return animationService.getAniAll();
    }

    // 누구든지 카테고리별 애니리스트 조회
    @GetMapping("/category-member")
    public CtAniListDto getAllCt(@RequestParam long aniCategoryIdx){

        return animationService.getMemCt(aniCategoryIdx);
    }


    // 결제회원만 애니 상세정보 보기
    @GetMapping("/info-member")
    public ResultAniInfo ForMember(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                   @RequestParam long aniIdx){
        return animationService.getForMember(memberKey,aniIdx);
    }

    @PostMapping("/save-time")
    public ResultDto saveTime(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                              @RequestBody PlayTimeDto playTimeDto){
        return animationService.saveRunningTime(memberKey,playTimeDto);
    }

    @PostMapping("/stop")
    public void stop(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                     @RequestBody StopAniDto stopAniDto){
        animationService.StopAnimation(memberKey,stopAniDto);
    }

    @GetMapping("/my-result")
    public AniConResultDto getMyData(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return animationService.myData(memberKey);
    }













}
