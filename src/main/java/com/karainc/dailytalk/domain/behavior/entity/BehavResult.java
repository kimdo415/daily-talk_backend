package com.karainc.dailytalk.domain.behavior.entity;


import com.karainc.dailytalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BehavResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="behav_result_id")
    private Long behavResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @Column(name="behav_date")
    private String behavDate;

    @Column(name = "next_date")
    private String nextDate;

    @Column(name = "during_date")
    private String duringDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "behavior_five",joinColumns =@JoinColumn(name ="behav_result_id" ))
    @Column(name ="behav_point")
    private List<String> behavPoint;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "behavior_stick",joinColumns =@JoinColumn(name ="behav_result_id" ))
    @Column(name ="behav_stick")
    private List<String> behavStick;


}
