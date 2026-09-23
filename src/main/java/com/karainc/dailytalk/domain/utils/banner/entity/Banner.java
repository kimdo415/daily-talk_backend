package com.karainc.dailytalk.domain.utils.banner.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerId;

    @Column(name = "banner_link")
    private String bannerLink;

    @Column(name="video_link")
    private String videoLink;
}
