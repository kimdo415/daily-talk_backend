package com.karainc.dailytalk.domain.contents.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="contents_idx")
    private Long contentsIdx;

    // 콘텐츠 이름
    @Column(name="contents_name")
    private String contentsName;

    //소개
    @Column(name="contents_intro")
    private String intro;

    // 가격
    @Column(name="price")
    private Integer price;

    // 이미지
    @Column(name = "content_image")
    private String contentsImage;

    // 유형 01 : 구독형 , 02: 상담콘텐츠 ,  03 : 진단검사 콘텐츠
    @Column(name = "content_type")
    private String contentType;

    // 구독형 상품일경우 들어가는 정보 : 구독기간
    @Column(name = "during_date")
    private String duringDate;

    // 상담/진담 검사일경우 들어가는 정보 : 검사 횟수
    @Column(name = "use_count")
    private Integer useCount;

    // 출력 유무
    @Column(name="view_status")
    private Boolean viewStatus;

    // 상품 생성일자
    @Column(name="date")
    private String date;

    // 판매 횟수
    @Column(name="sail_count")
    private Integer sailCount;
}
