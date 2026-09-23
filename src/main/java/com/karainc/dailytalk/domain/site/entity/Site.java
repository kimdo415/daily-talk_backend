package com.karainc.dailytalk.domain.site.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long siteId;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "youtube")
    private String youtube;

    @Column(name = "naver")
    private String naver;

    @Column(name = "facebook")
    private String facebook;

    @Column(name ="site_name")
    private String siteName;

    @Column(name="info")
    private String info;

    @Column(columnDefinition = "TEXT" , name = "tos")
    private String tos;

    @Column(columnDefinition = "TEXT" , name = "privacy")
    private String privacy;

    @Column(columnDefinition = "TEXT" , name = "children")
    private String children;

    @Column(columnDefinition = "TEXT" , name = "pledge")
    private String pledge;

    @Column(columnDefinition = "TEXT" ,name = "marketing")
    private String marketing;

    @Column(columnDefinition = "TEXT" ,name = "pay_agree")
    private String payAgree;
}
