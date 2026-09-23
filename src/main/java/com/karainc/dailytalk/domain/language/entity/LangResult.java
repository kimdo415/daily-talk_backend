package com.karainc.dailytalk.domain.language.entity;


import com.karainc.dailytalk.domain.behavior.entity.BehavResult;
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
public class LangResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lang_result_id")
    private Long langResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name="lang_date")
    private String langDate;

    @Column(name="next_date")
    private String nextDate;

    @Column(name="lang_point")
    private String langPoint;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "lang_stick",joinColumns =@JoinColumn(name ="lang_result_id" ))
    @Column(name ="lang_stick")
    private List<String> langStick;

    @Column(name="during_date")
    private String duringDate;

}
