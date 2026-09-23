package com.karainc.dailytalk.domain.category.controller.dto.response;

import com.karainc.dailytalk.domain.category.entitiy.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CateGoryResultDto {
    private String status;
    private String message;
    private List<Category> data;
}
