package com.karainc.dailytalk.domain.consult.entity;

import com.karainc.dailytalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="consultIdx")
    private Long consultIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rehab")
    Member rehab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consult_member")
    Member member;

    @Column(name = "detail",columnDefinition = "TEXT")
    private String detail;

    @Column(name ="start")
    private String startDate;

    @Column(name = "end")
    private String endDate;

    @Column(name ="status")
    private boolean view;
}
