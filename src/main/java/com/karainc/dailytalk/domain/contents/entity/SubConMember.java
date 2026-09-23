package com.karainc.dailytalk.domain.contents.entity;


import com.karainc.dailytalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubConMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="subcon_member_idx")
    private Long subConMemberIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name="end_date")
    private String endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Contents contents;
}
