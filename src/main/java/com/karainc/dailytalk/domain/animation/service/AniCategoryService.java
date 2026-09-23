package com.karainc.dailytalk.domain.animation.service;

import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.animation.controller.dto.requset.AniCtDto;
import com.karainc.dailytalk.domain.animation.controller.dto.requset.UpdateAniCtDto;
import com.karainc.dailytalk.domain.animation.controller.dto.response.AniCategoryListDto;
import com.karainc.dailytalk.domain.animation.controller.dto.response.AniCtResultDto;
import com.karainc.dailytalk.domain.animation.entity.AniCategory;
import com.karainc.dailytalk.domain.animation.repository.AniCategoryRepository;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AniCategoryService {
    private final AniCategoryRepository aniCategoryRepository;
    private final AdminService adminService;


    public AniCtResultDto make(String adminKey, AniCtDto aniCtDto){
        log.info("관리자 권한 : 애니메이션 카테고리 추가");
        adminService.checkAdmin(adminKey);
        if(aniCtDto.getCategory()==null || aniCtDto.getCategory().isEmpty()){
            throw new NoDataExceptionHandler("오류 : 애니메이션 카테고리 이름을 작성해주세요");
        }
        log.info("카테고리 생성: " + aniCtDto.getCategory());
        AniCategory aniCategory = AniCategory.builder()
                .aniCategoryName(aniCtDto.getCategory())
                .build();
        aniCategoryRepository.save(aniCategory);

        AniCtResultDto aniCtResultDto = new AniCtResultDto();
        aniCtResultDto.setStatus("200");
        aniCtResultDto.setMessage("애니메이션 카테고리 등록(관리자 권한)");
        aniCtResultDto.setData("애니메이션 카테고리 등록(관리자 권한)");
        return aniCtResultDto;
    }

    public AniCtResultDto update(String adminKey, UpdateAniCtDto updateAniCtDto){
        log.info("관리자 권한 : 애니메이션 카테고리 수정");
        adminService.checkAdmin(adminKey);
        AniCategory aniCategory = aniCategoryRepository.findByAniCategoryIdx(updateAniCtDto.getAniCategoryIdx());
        if(aniCategory==null){
            throw new NoDataExceptionHandler("오류 : 삭제된 애니매이션 카테고리");
        }
        String ani;
        if(updateAniCtDto.getAniCategoryName()==null || updateAniCtDto.getAniCategoryName().isEmpty()){
            ani=aniCategory.getAniCategoryName();
        }else{
            ani= updateAniCtDto.getAniCategoryName();;
        }
        log.info("변경된 애니메이션 카테고리 이름 : " + ani);
        log.info("애니메이션 위치 : " + aniCategory.getAniCategoryIdx());

        aniCategory.setAniCategoryName(ani);

        AniCtResultDto aniCtResultDto = new AniCtResultDto();
        aniCtResultDto.setStatus("200");
        aniCtResultDto.setMessage("애니메이션 카테고리 수정(관리자 권한)");
        aniCtResultDto.setData("애니메이션 카테고리 수정(관리자 권한");
        return aniCtResultDto;
    }

    public AniCategoryListDto ctList(String adminKey){
        log.info("관리자 권한 : 애니메이션 카테고리 리스트 조회");
        adminService.checkAdmin(adminKey);
        List<AniCategory> aniCategories = aniCategoryRepository.findAll();
        if(aniCategories==null){
            throw new NoDataExceptionHandler("등록된 애니메이션 카테고리가 없습니다");
        }
        AniCategoryListDto aniCategoryListDto = new AniCategoryListDto();
        aniCategoryListDto.setStatus("200");
        aniCategoryListDto.setMessage("애니메이션 카테고리 조회(관리자 권한)");
        aniCategoryListDto.setData(aniCategories);
        return aniCategoryListDto;
    }

    public AniCtResultDto ctDelete(String adminKey, long aniCategoryIdx){
        log.info("관리자 권한 : 애니메이션 카테고리 삭제");
        adminService.checkAdmin(adminKey);

        AniCategory aniCategory = aniCategoryRepository.findByAniCategoryIdx(aniCategoryIdx);
        if(aniCategory==null){
            throw new NoDataExceptionHandler("없는 애니메이션 카테고리 입니다");
        }
        log.info("삭제할 애니메이션 위치 : " + String.valueOf(aniCategoryIdx));

        aniCategoryRepository.delete(aniCategory);

        AniCtResultDto aniCtResultDto = new AniCtResultDto();
        aniCtResultDto.setStatus("200");
        aniCtResultDto.setMessage("애니메이션 카테고리 삭제(관리자 권한)");
        aniCtResultDto.setData("애니메이션 카테고리 수정(관리자 권한)");
        return aniCtResultDto;
    }

    public AniCategoryListDto getList(){
        log.info("회원 : 애니메이션 카테고리 리스트 조회");
        List<AniCategory> aniCategories = aniCategoryRepository.findAll();
        if(aniCategories==null){
            throw new NoDataExceptionHandler("현재 애니메이션 준비중입니다");
        }
        AniCategoryListDto aniCategoryListDto = new AniCategoryListDto();
        aniCategoryListDto.setStatus("200");
        aniCategoryListDto.setMessage("애니메이션 카테고리 조회");
        aniCategoryListDto.setData(aniCategories);
        return aniCategoryListDto;
    }

}
