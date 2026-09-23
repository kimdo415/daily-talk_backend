package com.karainc.dailytalk.domain.contents.controller.dto.data;


import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberViewList {
    private Long ContentsIdx;
    private String contentsName;
    private String contentsImage;
    private String intro;
    private Integer price;
}
