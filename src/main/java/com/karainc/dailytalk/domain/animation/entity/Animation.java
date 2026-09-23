package com.karainc.dailytalk.domain.animation.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Animation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ani_id")
    private Long aniIdx;

    @Column(name="ani_title")
    private String aniTitle;

    @Column(name="ani_sub_title")
    private String aniSubTitle;

    @Column(name = "ani_level")
    private String aniLevel;

    @Column(name = "ani_age")
    private String aniAge;

    @ManyToOne(fetch = FetchType.LAZY)
    AniCategory aniCategory;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="ani_urls" ,joinColumns = @JoinColumn(name="ani_id"))
    @Column(name = "animations")
    private List<String> animations;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="ani_names" ,joinColumns = @JoinColumn(name="ani_id"))
    @Column(name = "animation_name")
    private List<String> aniName;

    @Column(name = "ani_view_status")
    private boolean view;

    @Column(name = "ani_title_image")
    private String aniImage;

}
