package com.karainc.dailytalk.domain.category.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.category.controller.dto.requset.RequestCateGory;
import com.karainc.dailytalk.domain.category.controller.dto.requset.UpdateCateGoryDto;
import com.karainc.dailytalk.domain.category.controller.dto.response.CateGoryResultDto;
import com.karainc.dailytalk.domain.category.controller.dto.response.CtResultDto;
import com.karainc.dailytalk.domain.category.entitiy.Category;
import com.karainc.dailytalk.domain.category.repository.CategoryRepository;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.DataRedundancyHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.reflections.Reflections.log;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryService {
    private final AdminService adminService;
    private final CategoryRepository categoryRepository;


    public CtResultDto add(String adminKey, RequestCateGory requestCateGory){
        log.info("관리자 권한 : 카테고리 추가");
        adminService.checkAdmin(adminKey);
        if(requestCateGory.getCategory().equals("") || requestCateGory.getCategory().isEmpty()){
            throw new DataRedundancyHandler("오류: 카테고리 이름이 없습니다");
        }
        Category addCategory = Category.builder()
                .categoryName(requestCateGory.getCategory())
                .build();
        categoryRepository.save(addCategory);

        CtResultDto ctResultDto = new CtResultDto();
        ctResultDto.setStatus("200");
        ctResultDto.setMessage("카테고리 등록 완료");
        ctResultDto.setData("카테고리 등록 완료");
        return ctResultDto;
    }

    public CtResultDto change(String adminKey, UpdateCateGoryDto updateCateGoryDto){
        log.info("관리자 권한 : 카테고리 변경");
        adminService.checkAdmin(adminKey);
        Category category = categoryRepository.findByCategoryId(updateCateGoryDto.getCategoryId());
        if(category == null){
            throw new NoDataExceptionHandler("오류 : 삭제된 카테고리");
        }
        if(category.getCategoryName().isEmpty() || category.getCategoryName().equals("null")){
            throw new NoDataExceptionHandler("카테고리 이름을 넣어 주세요");
        }
        category.setCategoryName(updateCateGoryDto.getCategory());
        categoryRepository.save(category);
        CtResultDto ctResultDto = new CtResultDto();
        ctResultDto.setStatus("200");
        ctResultDto.setMessage("카테고리 수정 완료");
        ctResultDto.setData("카테고리 수종 완료");
        return ctResultDto;
    }

    public CateGoryResultDto getList(String adminKey){
        log.info("관리자 권한 : 카테고리 리스트 조회");
        adminService.checkAdmin(adminKey);
        List<Category> categoryList = categoryRepository.findAll();
        if(categoryList==null){
            throw new NoDataExceptionHandler("카테고리가 없습니다 먼저 카테로리를 만들어주세요");
        }
        CateGoryResultDto cateGoryResultDto = new CateGoryResultDto();
        cateGoryResultDto.setStatus("200");
        cateGoryResultDto.setMessage("카테고리 목록");
        cateGoryResultDto.setData(categoryList);
        return cateGoryResultDto;
    }

    public CtResultDto deleteCategory(String adminKey,Long categoryIdx){
        log.info("관리자 권한 : 카테고리 삭제");
        adminService.checkAdmin(adminKey);
        Category category = categoryRepository.findByCategoryId(categoryIdx);
        if(category==null){
            throw new NoDataExceptionHandler("삭제된 카테고리");
        }
        categoryRepository.delete(category);
        CtResultDto ctResultDto = new CtResultDto();
        ctResultDto.setStatus("200");
        ctResultDto.setMessage("카테고리 삭제 완료");
        ctResultDto.setData("카테고리 삭제 완료");
        return ctResultDto;
    }

    public CateGoryResultDto getListByMember(){
        List<Category> categoryList = categoryRepository.findAll();
        if(categoryList==null){
            throw new NoDataExceptionHandler("카테고리 준비중 입니다");
        }
        CateGoryResultDto cateGoryResultDto = new CateGoryResultDto();
        cateGoryResultDto.setStatus("200");
        cateGoryResultDto.setMessage("카테고리 목록");
        cateGoryResultDto.setData(categoryList);
        return cateGoryResultDto;
    }

}
