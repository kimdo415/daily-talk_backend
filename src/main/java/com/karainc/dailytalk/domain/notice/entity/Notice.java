package com.karainc.dailytalk.domain.notice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_idx")
    private Long noticeIdx;

    @Column(name="notice_title")
    private String noticeTitle;

    @Column(columnDefinition = "TEXT",name="notice_content")
    private String content;

    @Column(name = "notice_date")
    private String date;

    @Column(name="notice_thumbnail")
    private String thumbnail;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="notice_images" ,joinColumns = @JoinColumn(name="notice_idx"))
    @Column(name="notice_image")
    private List<String> images;

    @Column(name="notice_view")
    private boolean view;

    @Column(name="notice_type")
    private String type;
}
