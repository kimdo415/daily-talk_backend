package com.karainc.dailytalk.domain.pay.repository;

import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.pay.entity.PayMember;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayMemberRepository extends JpaRepository<PayMember ,Long> {

    PayMember findByMember(Member member);
}
