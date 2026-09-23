package com.karainc.dailytalk.domain.category.controller;


import com.karainc.dailytalk.domain.category.controller.dto.requset.RequestCateGory;
import com.karainc.dailytalk.domain.category.controller.dto.requset.UpdateCateGoryDto;
import com.karainc.dailytalk.domain.category.controller.dto.response.CateGoryResultDto;
import com.karainc.dailytalk.domain.category.controller.dto.response.CtResultDto;
import com.karainc.dailytalk.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/make")
    public CtResultDto create(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey, @RequestBody RequestCateGory requestCateGory){
        return categoryService.add(adminKey,requestCateGory);
    }

    @PutMapping("/update")
    public CtResultDto change(@RequestHeader(value = "X-ADMIN-TOKEN",required = false) String adminKey, @RequestBody UpdateCateGoryDto updateCateGoryDto){
        return categoryService.change(adminKey,updateCateGoryDto);
    }

    @GetMapping("/list")
    public CateGoryResultDto getAll(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false) String adminKey){
        return categoryService.getList(adminKey);
    }

    @DeleteMapping("/delete")
    public CtResultDto deleteCategory(@RequestHeader(value = "X-ADMIN-TOKEN" ,required = false)String adminKey,
                                      @RequestParam Long categoryIdx){
        return categoryService.deleteCategory(adminKey,categoryIdx);
    }

    @GetMapping("/list-for-member")
    public CateGoryResultDto getList(){
        return categoryService.getListByMember();
    }
}
