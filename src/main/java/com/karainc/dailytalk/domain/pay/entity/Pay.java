package com.karainc.dailytalk.domain.pay.entity;


import com.karainc.dailytalk.domain.contents.entity.Contents;
import com.karainc.dailytalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payIdx")
    // 주문 번호
    private Long payIdx;


    // 주문 회원
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 결제 날짜
    @Column
    private LocalDate authenticatedAt;

    // 결제 날짜
    @Column
    private Integer amount;

    // 상품코드
    @Column
    private Long contentCode;

    //주문번호
    @Column
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Contents payContents;

    @Column(name ="payment_key")
    private String paymentKey;

    @Column(name ="refund_status")
    private Boolean refundStatus;

    @Column(name="refund_amount")
    private Integer refundAmount;

    @Column(name="refund_date")
    private LocalDate refundDate;
}
