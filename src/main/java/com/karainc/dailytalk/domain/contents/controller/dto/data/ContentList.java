package com.karainc.dailytalk.domain.contents.controller.dto.data;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentList {
    private Long contentsIdx;
    private String contentsName;
    private Integer price;
    private Integer sailCount;
    private Boolean viewStatus;
    private String date;
}
