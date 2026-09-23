package com.karainc.dailytalk.domain.animation.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AniCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aniCategoryIdx;

    @Column
    private String aniCategoryName;
}
