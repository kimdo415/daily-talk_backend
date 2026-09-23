package com.karainc.dailytalk.domain.animation.entity;


import com.karainc.dailytalk.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AniData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ani_data_idx")
    private Long aniDataIdx;

    @Column(name = "ani_ref")
    private Long aniRef;

    @Column(name = "out_count")
    private Integer outCount;

    @Column(name ="replay_count")
    private Integer replayCount;

    @Column(name ="view_date")
    private LocalDate viewDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="ani_play_time" ,joinColumns = @JoinColumn(name="ani_data_idx"))
    @Column(name = "play_time")
    private List<Integer> playTime;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="ani_rate" ,joinColumns = @JoinColumn(name="ani_data_idx"))
    @Column(name = "rate")
    private List<Integer> rate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
