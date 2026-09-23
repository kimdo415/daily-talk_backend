package com.karainc.dailytalk.domain.pay.entity;


import com.karainc.dailytalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PayMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payMember_idx")
    private Long payMemberIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private String billingKey;

    @Column
    private Boolean repay;
}
