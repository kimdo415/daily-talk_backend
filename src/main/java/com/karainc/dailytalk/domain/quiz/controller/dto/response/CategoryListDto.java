package com.karainc.dailytalk.domain.quiz.controller.dto.response;

import com.karainc.dailytalk.domain.quiz.controller.dto.data.CategoryDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryListDto {
    private String status;
    private String message;
    private List<CategoryDto> data;
}
