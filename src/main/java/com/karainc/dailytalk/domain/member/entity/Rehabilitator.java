package com.karainc.dailytalk.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Rehabilitator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rehabilitator_idx")
    private Long rehabilIdx;

    @Column
    private Long refId;

    @Column
    private String region;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="rehab_career" ,joinColumns = @JoinColumn(name="rehabilitator_idx"))
    @Column(name="career")
    // 경력 내용
    private List<String> career;

    // 경력 날짜
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="rehab_career_day" ,joinColumns = @JoinColumn(name="rehabilitator_idx"))
    @Column(name="career_Day")
    private List<String> careerDay;

    // 분과
    private String division;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="rehab_cert" ,joinColumns = @JoinColumn(name="rehabilitator_idx"))
    @Column(name="cert")

    // 자격증
    private List<String> cert;

    @Column
    private String day;

    @Column
    private boolean status;

    @Column
    private String checkEnum;

    @Column
    private String detailAddress;

}
