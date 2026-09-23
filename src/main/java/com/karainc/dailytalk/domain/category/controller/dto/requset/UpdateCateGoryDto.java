package com.karainc.dailytalk.domain.category.controller.dto.requset;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCateGoryDto {
    private Long categoryId;
    private String category;
}
